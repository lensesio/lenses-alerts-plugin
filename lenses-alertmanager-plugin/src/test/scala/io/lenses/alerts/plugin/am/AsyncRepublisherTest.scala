package io.lenses.alerts.plugin.am

import java.util
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

import io.lenses.alerting.plugin.AlertLevel
import io.lenses.alerting.plugin.scalaapi.Alert
import io.lenses.alerts.plugin.am.AlertManagerAlert._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.jdk.CollectionConverters._
import scala.util.Success
import scala.util.Try

class AsyncRepublisherTest extends AnyFunSuite with Matchers {
  val config = Config(List("http://machine:12333"), "prod1", "http://lensesisgreat:42424", 10000, HttpConfig())

  test("pushes all the alerts to the Publisher") {
    val alert1 = Alert(
      AlertLevel.CRITICAL,
      "infrastructure",
      List("tag1"),
      "prod1",
      "This is important",
      None,
      System.currentTimeMillis(),
      1,
      Map.empty).asJava.toAMAlert(config)

    val alert2 = Alert(
      AlertLevel.HIGH,
      "infrastructure",
      List("tag1"),
      "prod1",
      "This is important",
      None,
      System.currentTimeMillis(),
      2,
      Map.empty).asJava.toAMAlert(config)

    val alertsRaised = new AlertsRaised {
      override def getAlerts: Iterable[AlertManagerAlert] = List(alert1, alert2)
    }

    val buffer = new java.util.concurrent.LinkedBlockingQueue[AlertManagerAlert]()
    val latch = new CountDownLatch(alertsRaised.getAlerts.size)
    val publisher = new Publisher {
      override def publish(alert: AlertManagerAlert): Try[Unit] = {
        buffer.put(alert)
        latch.countDown()
        Success(())
      }
      override def close(): Unit = {}
    }

    val republisher = AsyncRepublisher(publisher, 2000, alertsRaised)
    latch.await(3000, TimeUnit.MILLISECONDS) shouldBe true

    val drained = new util.ArrayList[AlertManagerAlert]()
    val drainedCount = buffer.drainTo(drained)
    drainedCount shouldBe 2
    republisher.close()

    Thread.sleep(2500)
    buffer.size() shouldBe 0

    drained.asScala.toList shouldBe List(alert1, alert2)
  }
}
