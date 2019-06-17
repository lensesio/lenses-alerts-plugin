package io.lenses.alerts.plugin.am

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import io.lenses.alerting.plugin
import io.lenses.alerting.plugin.javaapi.util.{Failure => JFailure}
import io.lenses.alerting.plugin.javaapi.util.{Success => JSuccess}
import io.lenses.alerting.plugin.scalaapi.Alert
import io.lenses.alerting.plugin.AlertLevel
import io.lenses.alerts.plugin.am.AlertManagerAlert._
import org.scalatest.FunSuite
import org.scalatest.Matchers

import scala.collection.mutable.ListBuffer
import scala.util.Success
import scala.util.Try

class AlertManagerAlertServiceTest extends FunSuite with Matchers {
  test("pushes the alert to the publisher when it is received") {
    val config = Config(List("http://machine:12333"), "prod1", "http://lensesisgreat:42424", 10000, HttpConfig())
    val countdown = new CountDownLatch(1)
    val buffer = ListBuffer.empty[AlertManagerAlert]
    val publisher = new Publisher {
      override def publish(alert: AlertManagerAlert): Try[Unit] = {
        buffer += alert
        countdown.countDown()
        Success(())
      }
      override def close(): Unit = {}
    }

    val service = new AlertManagerService(config, publisher, (_, _, _) => NoOpRepublisher)

    val alert = Alert(
      AlertLevel.CRITICAL,
      "infrastructure",
      List("tag1"),
      "prod1",
      "This is important",
      None,
      1,
      Map.empty).asJava

    publish(alert, service)

    countdown.await(500, TimeUnit.MILLISECONDS) shouldBe true
    service.close()
    buffer.size shouldBe 1
    buffer.head shouldBe alert.toAMAlert.copy(startsAt = buffer.head.startsAt)
    buffer.head.labels
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

    val service = new AlertManagerService(config, publisher, (_, _, _) => NoOpRepublisher)

    val alert1 = Alert(
      AlertLevel.CRITICAL,
      "infrastructure",
      List("tag1"),
      "prod1",
      "This is important",
      None,
      1,
      Map.empty).asJava

    publish(alert1, service)

    val alert2 = Alert(
      AlertLevel.CRITICAL,
      "infrastructure",
      List("tag1"),
      "prod1",
      "This is important",
      None,
      2,
      Map.empty).asJava

    publish(alert2, service)

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
      2,
      Map.empty)

    buffer.clear()
    publish(alert2Info.asJava, service)

    buffer.size shouldBe 1
    buffer.head shouldBe alert2Info.copy(level = AlertLevel.CRITICAL).asJava.toAMAlert.copy(startsAt = buffer.head.startsAt, endsAt = buffer.head.endsAt)
    buffer.head.endsAt.isDefined shouldBe true
  }

  private def publish(alert: plugin.Alert, service: AlertManagerService): Unit = {
    service.publish(alert) match {
      case _: JSuccess[_] =>
      case _: JFailure[_] => fail("There should be no failure.")
    }
  }
}
