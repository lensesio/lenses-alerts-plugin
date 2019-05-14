/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.scalaapi

import io.lenses.alerting.plugin.javaapi.util.{Failure => JFailure, Success => JSuccess}
import scala.util.{Failure, Success}

class AlertingPluginSpec extends AlertingSpecBase {

  "init" should {
    "succeed on underlying plugin init success" in new TestContext {

      val service = AlertingPlugin(_ => new JSuccess(forwardingService)).init(Map.empty)

      service shouldBe 'success
      service.flatMap(_.publish(dummyAlert)) shouldBe Success(dummyAlert)
    }

    "fail on underlying plugin init failure" in new TestContext {
      val plugin = AlertingPlugin(_ => new JFailure(anException))

      plugin.init(Map.empty) shouldBe Failure(anException)
    }

    "fail on underlying plugin init raises exception" in new TestContext {
      val plugin = AlertingPlugin(_ => throw anException)

      plugin.init(Map.empty) shouldBe Failure(anException)
    }
  }
}
