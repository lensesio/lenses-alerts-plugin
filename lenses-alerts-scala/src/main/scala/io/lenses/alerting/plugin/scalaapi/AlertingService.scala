/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.scalaapi

import io.lenses.alerting.plugin.javaapi.{AlertingService => JAlertingService}
import io.lenses.alerting.plugin.scalaapi.util.TryUtils._

import scala.util.Try

/**
  * simple alerting service wrapper giving Scala users a bit more type information
  */
class AlertingService(underlying: JAlertingService) {

  def publish(alert: Alert): Try[Alert] = Try { underlying.publish(alert).asScala }.flatten
}

object AlertingService {
  def apply(underlying: JAlertingService): AlertingService = new AlertingService(underlying)
}