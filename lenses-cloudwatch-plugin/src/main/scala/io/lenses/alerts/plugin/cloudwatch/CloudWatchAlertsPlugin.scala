package io.lenses.alerts.plugin.cloudwatch

import java.util

import io.lenses.alerting.plugin.javaapi.AlertingPlugin
import io.lenses.alerting.plugin.javaapi.AlertingService
import io.lenses.alerting.plugin.javaapi.ConfigEntry
import io.lenses.alerting.plugin.javaapi.util.{ Try => JTry }
import io.lenses.alerts.plugin.cloudwatch.TryUtils._

import scala.jdk.CollectionConverters._
import scala.util.Try

class CloudWatchAlertsPlugin extends AlertingPlugin with Metadata {
  import CloudWatchAlertsPlugin._

  override val name: String = "CloudWatch Events"

  override val description: String = "Plugin to support pushing Lenses alerts to CloudWatch events"

  override def init(config: util.Map[String, String]): JTry[AlertingService] = Try {
    val map = config.asScala

    def getOrError(key: String): String =
      map.getOrElse(key, throw new IllegalArgumentException(s"Invalid configuration for CloudWatch plugin'[$key]'"))

    val authMode = getOrError(AUTH_MODE_KEY).toUpperCase
    val (accessKey, accessSecretKey) = authMode match {
      case "IAM" =>
        if (map.contains(ACCESS_KEY) || map.contains(ACCESS_SECRET_KEY))
          throw new IllegalArgumentException(s"Invalid configuration for CloudWatch plugin'[$AUTH_MODE_KEY]'")
        None -> None
      case "BASIC" => Some(getOrError(ACCESS_KEY)) -> Some(getOrError(ACCESS_SECRET_KEY))
    }

    val region     = map.get(REGION)
    val sourceName = getOrError(SOURCE)

    val cwConfig = CloudWatchConfig(accessKey, accessSecretKey, region, sourceName)
    val as: AlertingService = new CloudWatchAlertService(name, description, cwConfig)
    as
  }.asJava

  override def configKeys(): util.List[ConfigEntry] = {
    import scala.jdk.CollectionConverters._
    List(
      new ConfigEntry(ACCESS_KEY, "Contains the AWS access key of an IAM account."),
      new ConfigEntry(ACCESS_SECRET_KEY, "Contains the AWS access secret key of an IAM account."),
      new ConfigEntry(SOURCE, "Contains the source name of ta CloudWatch event."),
    ).asJava
  }
}

object CloudWatchAlertsPlugin {
  val AUTH_MODE_KEY     = "authentication-mode"
  val ACCESS_KEY        = "access-key"
  val ACCESS_SECRET_KEY = "access-secret-key"
  val REGION            = "region"
  val SOURCE            = "source"
}
