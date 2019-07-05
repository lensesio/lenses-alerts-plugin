/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.scalaapi

import java.util.Collections
import java.util.UUID

import io.lenses.alerting.plugin.{Alert => JAlert}
import io.lenses.alerting.plugin.javaapi.{AlertingService => JAlertingService}
import io.lenses.alerting.plugin.javaapi.util.{Success => JSuccess}
import io.lenses.alerting.plugin.javaapi.util.Try
import io.lenses.alerting.plugin.AlertLevel
import org.scalatest.Matchers
import org.scalatest.WordSpec

trait AlertingSpecBase extends WordSpec with Matchers {

  trait TestContext {
    private def randomStr = UUID.randomUUID().toString

    val anException = new Exception(randomStr)

    val dummyAlert = Alert(AlertLevel.INFO, "", Nil, "", "", None, 0, 0, Map.empty)

    val forwardingService = new JAlertingService {
      override def publish(alert: JAlert): Try[JAlert] = new JSuccess(alert)
      override def name(): String = ""
      override def description(): String = ""
      override def displayedInformation(): java.util.Map[String, String] = Collections.emptyMap()
    }
  }

}
