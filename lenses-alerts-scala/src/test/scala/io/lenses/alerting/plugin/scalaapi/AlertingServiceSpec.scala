/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.scalaapi

import java.util.Collections

import io.lenses.alerting.plugin.{Alert => JAlert}
import io.lenses.alerting.plugin.javaapi.{AlertingService => JAlertingService}
import io.lenses.alerting.plugin.javaapi.util.{Failure => JFailure}
import io.lenses.alerting.plugin.javaapi.util.Try
import io.lenses.alerting.plugin.scalaapi.AlertConversion._

import scala.util.Failure
import scala.util.Success

class AlertingServiceSpec extends AlertingSpecBase {

  "publish" should {
    "succeed on underlying service publish success" in new TestContext {
      val service = AlertingService(forwardingService)

      service.publish(dummyAlert.asJava).map(_.asScala) shouldBe Success(dummyAlert)
    }

    "fail on underlying service publish failure" in new TestContext {
      val service = new AlertingService(new JAlertingService {
        override def publish(alert: JAlert): Try[JAlert] = new JFailure(anException)
        override def name(): String = ""
        override def description(): String = ""
        override def displayedInformation(): java.util.Map[String, String] = Collections.emptyMap()
      })

      service.publish(dummyAlert.asJava) shouldBe Failure(anException)
    }

    "fail on underlying service publish raises exception" in new TestContext {
      val service = new AlertingService(new JAlertingService {
        override def publish(alert: JAlert): Try[JAlert] = throw anException
        override def name(): String = ""
        override def description(): String = ""
        override def displayedInformation(): java.util.Map[String, String] = Collections.emptyMap()
      })

      service.publish(dummyAlert.asJava) shouldBe Failure(anException)
    }
  }
}
