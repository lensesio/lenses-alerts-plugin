/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.scalaapi

import io.lenses.alerting.plugin
import io.lenses.alerting.plugin.javaapi.util.{Failure => JFailure}
import io.lenses.alerting.plugin.javaapi.util.{Success => JSuccess}

import scala.collection.JavaConverters._
import scala.util.Failure
import scala.util.Success
import scala.util.Try
import AlertConversion._

class AlertingPluginSpec extends AlertingSpecBase {

  "init" should {
    "succeed on underlying plugin init success" in new TestContext {

      val service = AlertingPlugin(new TestBasePlugin(new JSuccess(forwardingService))).init(Map.empty, None, None)

      service shouldBe 'success
      val result: Try[plugin.Alert] = service.flatMap(_.publish(dummyAlert.asJava))
      result.isSuccess shouldBe true
      val alert: plugin.Alert = result.get
      alert.asScala shouldBe dummyAlert
    }

    "fail on underlying plugin init failure" in new TestContext {
      val plugin = AlertingPlugin(new TestBasePlugin(new JFailure(anException)))

      plugin.init(Map.empty, None, None) shouldBe Failure(anException)
    }

    "fail on underlying plugin init raises exception" in new TestContext {
      val plugin = AlertingPlugin(new TestBasePlugin(throw anException))

      plugin.init(Map.empty, None, None) shouldBe Failure(anException)
    }
  }
}
