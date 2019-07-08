package io.lenses.alerts.plugin.am

import java.util

import io.lenses.alerting.plugin.javaapi.AlertingPlugin
import io.lenses.alerting.plugin.javaapi.util.{Try => JTry}
import io.lenses.alerting.plugin.javaapi.AlertingService
import io.lenses.alerting.plugin.javaapi.ConfigEntry
import io.lenses.alerts.plugin.am.TryUtils._

import scala.collection.JavaConverters._
import scala.util.Try

class AlertManagerPlugin extends AlertingPlugin with Metadata {
  override def init(config: util.Map[String, String]): JTry[AlertingService] = Try {
    val amConfig = Config.from(config.asScala.toMap)
    val publisher = new HttpPublisher(amConfig)
    val as: AlertingService = new AlertManagerService(amConfig, publisher)
    as
  }.asJava

  override def configKeys(): util.List[ConfigEntry] = {
    List(
      new ConfigEntry("endpoints", "Comma separated Alert Manager endpoints"),
      new ConfigEntry("source", "The Lenses instance raising the alert"),
      new ConfigEntry("generator.url", "A URL to identify Lenses; it can be left to empty"),
      new ConfigEntry("ssl", "If SSL connection is required set the value to true."),
      new ConfigEntry("publish.interval", "The time window in milliseconds to republish raised alerts. Alert Manager requires opened alerts to be sent over and over. Default value is 300000 - which is 5 minutes"),
      new ConfigEntry("http.timeout.connect", "The amount of time in milliseconds to open the HTTP connection to the Alert Manager endpoints. Default value is 5000 - which is 5 seconds"),
      new ConfigEntry("http.timeout.request", "The amount of time in milliseconds to wait for a response from Alert Manager. Default value is 15000 - which is 15 seconds"),
      new ConfigEntry("http.timeout.socket", "The amount of time in milliseconds to wait in order to open the underlying HTTP connection socket. Default value is 5000 - which is 5 seconds")
    ).asJava
  }
}
