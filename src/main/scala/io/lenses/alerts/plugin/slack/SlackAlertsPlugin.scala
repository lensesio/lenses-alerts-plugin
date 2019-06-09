package io.lenses.alerts.plugin.slack

import java.util

import io.lenses.alerting.plugin.javaapi.AlertingPlugin
import io.lenses.alerting.plugin.javaapi.util.{Try => JTry}
import io.lenses.alerting.plugin.javaapi.AlertingService
import io.lenses.alerts.plugin.slack.SlackAlertsPlugin._
import io.lenses.alerts.plugin.slack.TryUtils._

import scala.collection.JavaConverters._
import scala.util.Try

class SlackAlertsPlugin extends AlertingPlugin {
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
}

object SlackAlertsPlugin {
  val WEBHOOK = "webhook.url"
  val USER = "username"
  val CHANNEL = "channel"
  val ICON = "icon.url"
}