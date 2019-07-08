package io.lenses.alerts.plugin.slack

import java.util

import io.lenses.alerting.plugin.javaapi.AlertingPlugin
import io.lenses.alerting.plugin.javaapi.util.{Try => JTry}
import io.lenses.alerting.plugin.javaapi.AlertingService
import io.lenses.alerting.plugin.javaapi.ConfigEntry
import io.lenses.alerts.plugin.slack.SlackAlertsPlugin._
import io.lenses.alerts.plugin.slack.TryUtils._

import scala.collection.JavaConverters._
import scala.util.Try

class SlackAlertsPlugin extends AlertingPlugin with Metadata {
  override def init(config: util.Map[String, String]): JTry[AlertingService] = Try {
    val map = config.asScala
    def getOrError(key: String): String = {
      map.getOrElse(key, throw new IllegalArgumentException(s"Invalid configuration for Slack plugin'[$key]'"))
    }

    val webhookUrl = getOrError(WEBHOOK)
    val userName = getOrError(USER)
    val channel = getOrError(CHANNEL)
    val iconUrl = map.get(ICON)

    val as: AlertingService = new SlackAlertService(SlackConfig(webhookUrl, userName, channel, iconUrl))
    as
  }.asJava
  override def configKeys(): util.List[ConfigEntry] = {
    import scala.collection.JavaConverters._
    List(
      new ConfigEntry(WEBHOOK ,"Contains the Slack connection endpoint"),
      new ConfigEntry(USER,"When alerts are sent to the channel, this settings contains the user name sending them."),
      new ConfigEntry(CHANNEL,"The name of the Slack channel to send the alerts to."),
      new ConfigEntry(ICON,"Path to the icon to use for the alerts message header once they are pushed to Slack."),
    ).asJava
  }
}

object SlackAlertsPlugin {
  val WEBHOOK = "webhook.url"
  val USER = "username"
  val CHANNEL = "channel"
  val ICON = "icon.url"
}