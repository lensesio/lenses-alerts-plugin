package io.lenses.alerts.plugin.am

import java.util

import io.lenses.alerting.plugin.javaapi.util.{Try => JTry}
import io.lenses.alerting.plugin.javaapi.{AlertingPlugin, AlertingService, ConfigEntry}
import io.lenses.alerts.plugin.am.TryUtils._

import scala.collection.JavaConverters._
import scala.collection.concurrent.TrieMap
import scala.util.Try

object AlertManagerPlugin {

  val sharedRaisedAlertsBuffer: TrieMap[Int, AlertManagerAlert] =
    scala.collection.concurrent.TrieMap.empty[Int, AlertManagerAlert]

}

class AlertManagerPlugin extends AlertingPlugin with Metadata {

  override val name: String = "Alert Manager"

  override val description: String = "Plugin to support pushing Lenses alerts to Prometheus Alert Manager"

  override def init(config: util.Map[String, String]): JTry[AlertingService] = Try {
    val amConfig = Config.from(config.asScala.toMap)
    val publisher = new HttpPublisher(amConfig)
    val as: AlertingService = new AlertManagerService(
      name,
      description,
      amConfig,
      publisher,
      AlertManagerPlugin.sharedRaisedAlertsBuffer
    )
    as
  }.asJava

  override def configKeys(): util.List[ConfigEntry] = {
    List(
      new ConfigEntry(Config.Endpoints, "Comma separated Alert Manager endpoints"),
      new ConfigEntry(Config.Source, "The Lenses instance raising the alert"),
      new ConfigEntry(Config.GeneratorUrl, "A URL to identify Lenses; it can be left to empty"),
      new ConfigEntry(Config.SSL, "If SSL connection is required set the value to true."),
      new ConfigEntry(Config.PublishInterval, "The time window in milliseconds to republish raised alerts. Alert Manager requires opened alerts to be sent over and over. Default value is 300000 - which is 5 minutes"),
      new ConfigEntry(Config.HttpConnectTimeout, "The amount of time in milliseconds to open the HTTP connection to the Alert Manager endpoints. Default value is 5000 - which is 5 seconds"),
      new ConfigEntry(Config.HttpRequestTimeout, "The amount of time in milliseconds to wait for a response from Alert Manager. Default value is 15000 - which is 15 seconds"),
      new ConfigEntry(Config.HttpSocketTimeout, "The amount of time in milliseconds to wait in order to open the underlying HTTP connection socket. Default value is 5000 - which is 5 seconds")
    ).asJava
  }
}
