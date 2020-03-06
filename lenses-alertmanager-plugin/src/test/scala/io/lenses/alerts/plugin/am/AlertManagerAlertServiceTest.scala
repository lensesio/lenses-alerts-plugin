package io.lenses.alerts.plugin.am

import io.lenses.alerting.plugin.AlertLevel
import io.lenses.alerting.plugin.javaapi.util.{Failure => JFailure}
import io.lenses.alerting.plugin.javaapi.util.{Success => JSuccess}
import io.lenses.alerting.plugin.scalaapi.Alert
import io.lenses.alerts.plugin.am.AlertManagerAlert._
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.collection.mutable.ListBuffer
import scala.util.Failure
import scala.util.Success
import scala.util.Try

class AlertManagerAlertServiceTest extends FunSuite with Matchers {
  test("pushes the alert to the publisher when it is received") {
    val config = Config(List("http://machine:12333"), "prod1", "http://lensesisgreat:42424", 10000, HttpConfig())
    val buffer = ListBuffer.empty[AlertManagerAlert]
    val publisher = new Publisher {
      override def publish(alert: AlertManagerAlert): Try[Unit] = {
        buffer += alert
        Success(())
      }
      override def close(): Unit = {}
    }

    val service = new AlertManagerService("", "", config, publisher, (_, _, _) => NoOpRepublisher)

    val alert = Alert(
      AlertLevel.CRITICAL,
      "infrastructure",
      List("tag1"),
      "prod1",
      "This is important",
      None,
      System.currentTimeMillis(),
      1,
      Map.empty).asJava

    service.publish(alert) match {
      case _: JSuccess[_] =>
      case _: JFailure[_] => fail("There should be no failure.")
    }

    service.close()
    buffer.size shouldBe 1
    buffer.head shouldBe alert.toAMAlert.copy(startsAt = buffer.head.startsAt)
    buffer.head.labels
  }

  test("returns failure if publish fails") {
    val config = Config(List("http://machine:12333"), "prod1", "http://lensesisgreat:42424", 10000, HttpConfig())
    val theException = new Exception("Ooops!")
    val publisher = new Publisher {
      override def publish(alert: AlertManagerAlert): Try[Unit] = {
        Failure(theException)
      }
      override def close(): Unit = {}
    }

    val service = new AlertManagerService("", "", config, publisher, (_, _, _) => NoOpRepublisher)

    val alert = Alert(
      AlertLevel.CRITICAL,
      "infrastructure",
      List("tag1"),
      "prod1",
      "This is important",
      None,
      System.currentTimeMillis(),
      1,
      Map.empty).asJava

    service.publish(alert) match {
      case _: JSuccess[_] => fail("Publish should fail")
      case f: JFailure[_] => f.exception shouldBe theException
    }
  }

  test("keeps the alerts raised until an INFO version is received") {
    val config = Config(List("http://machine:12333"), "prod1", "http://lensesisgreat:42424", 1000, HttpConfig())

    val buffer = ListBuffer.empty[AlertManagerAlert]

    val publisher = new Publisher {
      override def publish(alert: AlertManagerAlert): Try[Unit] = {
        buffer += alert
        Success(())
      }
      override def close(): Unit = {}
    }

    val service = new AlertManagerService("", "", config, publisher, (_, _, _) => NoOpRepublisher)

    val alert1 = Alert(
      AlertLevel.CRITICAL,
      "infrastructure",
      List("tag1"),
      "prod1",
      "This is important",
      None,
      System.currentTimeMillis(),
      1,
      Map.empty).asJava

    service.publish(alert1) match {
      case _: JSuccess[_] =>
      case _: JFailure[_] => fail("There should be no failure.")
    }

    val alert2 = Alert(
      AlertLevel.CRITICAL,
      "infrastructure",
      List("tag1"),
      "prod1",
      "This is important",
      None,
      System.currentTimeMillis(),
      2,
      Map.empty).asJava

    service.publish(alert2) match {
      case _: JSuccess[_] =>
      case _: JFailure[_] => fail("There should be no failure.")
    }

    buffer.size shouldBe 2

    val copied1 = alert1.toAMAlert.copy(startsAt = buffer.head.startsAt)
    val copied2 = alert2.toAMAlert.copy(startsAt = buffer(1).startsAt)
    buffer.sortBy(_.alertId) shouldBe ListBuffer(copied1, copied2)

    val alert2Info = Alert(
      AlertLevel.INFO,
      "infrastructure",
      List("tag1"),
      "prod1",
      "This is important",
      None,
      System.currentTimeMillis(),
      2,
      Map.empty)

    buffer.clear()
    service.publish(alert2Info.asJava) match {
      case _: JSuccess[_] =>
      case _: JFailure[_] => fail("There should be no failure.")
    }

    buffer.size shouldBe 1
    buffer.head shouldBe alert2Info.copy(level = AlertLevel.CRITICAL).asJava.toAMAlert.copy(startsAt = buffer.head.startsAt, endsAt = buffer.head.endsAt)
    buffer.head.endsAt.isDefined shouldBe true
  }

  }
