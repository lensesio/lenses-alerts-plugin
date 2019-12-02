package io.lenses.alerts.plugin.cloudwatch

import java.util

import io.lenses.alerting.plugin.javaapi.{AlertingPlugin, AlertingService, ConfigEntry}
import io.lenses.alerting.plugin.javaapi.util.{Try => JTry}
import io.lenses.alerts.plugin.cloudwatch.TryUtils._
import CloudWatchAlertsPlugin.{ACCESS_KEY, ACCESS_SECRET_KEY, SOURCE}

import scala.collection.JavaConverters._
import scala.util.Try

class CloudWatchAlertsPlugin extends AlertingPlugin with Metadata {

  override val name: String = "CloudWatch Events"

  override val description: String = "Plugin to support pushing Lenses alerts to CloudWatch events"

  override def init(config: util.Map[String, String]): JTry[AlertingService] = Try {
    val map = config.asScala

    def getOrError(key: String): String = {
      map.getOrElse(key, throw new IllegalArgumentException(s"Invalid configuration for CloudWatch plugin'[$key]'"))
    }

    val accessKey = getOrError(ACCESS_KEY)
    val accessSecretKey = getOrError(ACCESS_SECRET_KEY)
    val sourceName = getOrError(SOURCE)

    val cwConfig = CloudWatchConfig(accessKey, accessSecretKey, sourceName)
    val as: AlertingService = new CloudWatchAlertService(name, description, cwConfig)
    as
  }.asJava

  override def configKeys(): util.List[ConfigEntry] = {
    import scala.collection.JavaConverters._
    List(
      new ConfigEntry(ACCESS_KEY, "Contains the AWS access key of an IAM account."),
      new ConfigEntry(ACCESS_SECRET_KEY, "Contains the AWS access secret key of an IAM account."),
      new ConfigEntry(SOURCE, "Contains the source name of ta CloudWatch event."),
    ).asJava
  }
}

object CloudWatchAlertsPlugin {
  val ACCESS_KEY = "access-key"
  val ACCESS_SECRET_KEY = "access-secret-key"
  val SOURCE = "source"
}
