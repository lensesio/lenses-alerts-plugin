/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.cloudwatch

import io.lenses.alerting.plugin.javaapi.util.Success
import io.lenses.alerting.plugin.javaapi.util.Failure
import io.lenses.alerts.plugin.cloudwatch.CloudWatchAlertsPlugin
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.util.UUID
import scala.jdk.CollectionConverters._

class CloudWatchAlertsPluginSpec extends AnyWordSpec with Matchers {

  "init" should {
    "success when authentication mode set to IAM and no secret and secret key are defined" in new TestContext {

      val result = cloudWatchPlugin.init(
        scala.collection.immutable.Map(
          CloudWatchAlertsPlugin.AUTH_MODE_KEY -> "IAM",
          CloudWatchAlertsPlugin.REGION        -> "eu-west-2",
          CloudWatchAlertsPlugin.SOURCE        -> "customSource",
        ).asJava,
      )

      result shouldBe a[Success[_]]
    }

    "success when authentication mode set to BASIC and access key and secret access key are defined" in new TestContext {

      val result = cloudWatchPlugin.init(
        scala.collection.immutable.Map(
          CloudWatchAlertsPlugin.AUTH_MODE_KEY     -> "BASIC",
          CloudWatchAlertsPlugin.ACCESS_KEY        -> UUID.randomUUID().toString,
          CloudWatchAlertsPlugin.ACCESS_SECRET_KEY -> UUID.randomUUID().toString,
          CloudWatchAlertsPlugin.REGION            -> "eu-west-2",
          CloudWatchAlertsPlugin.SOURCE            -> "customSource",
        ).asJava,
      )

      result shouldBe a[Success[_]]
    }

    "fail when authentication mode set to BASIC and secret and secret key are not defined" in new TestContext {

      val result = cloudWatchPlugin.init(
        scala.collection.immutable.Map(
          CloudWatchAlertsPlugin.AUTH_MODE_KEY -> "BASIC",
          CloudWatchAlertsPlugin.REGION        -> "eu-west-2",
          CloudWatchAlertsPlugin.SOURCE        -> "customSource",
        ).asJava,
      )

      result shouldBe a[Failure[_]]
    }

    "fail when authentication mode set to IAM and secret and secret key are defined" in new TestContext {

      val result = cloudWatchPlugin.init(
        scala.collection.immutable.Map(
          CloudWatchAlertsPlugin.AUTH_MODE_KEY     -> "IAM",
          CloudWatchAlertsPlugin.ACCESS_KEY        -> UUID.randomUUID().toString,
          CloudWatchAlertsPlugin.ACCESS_SECRET_KEY -> UUID.randomUUID().toString,
          CloudWatchAlertsPlugin.REGION            -> "eu-west-2",
          CloudWatchAlertsPlugin.SOURCE            -> "customSource",
        ).asJava,
      )

      result shouldBe a[Failure[_]]
    }
  }

  trait TestContext {
    val cloudWatchPlugin = new CloudWatchAlertsPlugin()
  }
}
