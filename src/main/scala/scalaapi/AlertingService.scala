package io.lenses.alerting.plugin.scalaapi

import io.lenses.alerting.plugin.{Alert, AlertingService => JAlertingService}

import scala.util.Try

/**
  * simple alerting service wrapper giving Scala users a bit more type information
  */
class AlertingService(service: JAlertingService) {

  def publish(alert: Alert): Try[Unit] = Try { service.publish(alert) }
}