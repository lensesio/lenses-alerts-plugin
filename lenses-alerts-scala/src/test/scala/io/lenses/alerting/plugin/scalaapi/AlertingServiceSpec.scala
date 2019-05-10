/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.scalaapi

import io.lenses.alerting.plugin
import io.lenses.alerting.plugin.javaapi.{AlertingService => JAlertingService}
import io.lenses.alerting.plugin.javaapi.util.{Try, Failure => JFailure}

import scala.util.{Failure, Success}

class AlertingServiceSpec extends AlertingSpecBase {

  "publish" should {
    "succeed on underlying service publish success" in new TestContext {
      val service = AlertingService(forwardingService)

      service.publish(dummyAlert) shouldBe Success(dummyAlert)
    }

    "fail on underlying service publish failure" in new TestContext {
      val service = new AlertingService(new JAlertingService {
        override def publish[T <: plugin.Alert](alert: T): Try[T] = new JFailure(anException)
      })

      service.publish(dummyAlert) shouldBe Failure(anException)
    }

    "fail on underlying service publish raises exception" in new TestContext {
      val service = new AlertingService(new JAlertingService {
        override def publish[T <: plugin.Alert](alert: T): Try[T] = throw anException
      })

      service.publish(dummyAlert) shouldBe Failure(anException)
    }
  }
}
