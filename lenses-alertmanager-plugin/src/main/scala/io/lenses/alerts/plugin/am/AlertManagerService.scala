package io.lenses.alerts.plugin.am

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util

import io.lenses.alerting.plugin.{Alert, AlertLevel}
import io.lenses.alerting.plugin.javaapi.AlertingService
import io.lenses.alerting.plugin.javaapi.util.{Try => JTry}
import io.lenses.alerts.plugin.am.AlertManagerAlert._
import io.lenses.alerts.plugin.am.TryUtils._

import scala.util.Success

class AlertManagerService(override val name: String,
                          override val description: String,
                          config: Config,
                          amPublisher: Publisher,
                          republisherFn: (Publisher, Long, AlertsRaised) => Republisher = Republisher.async)
  extends AlertingService with AutoCloseable with AlertsRaised with Metadata {

  private val raisedAlertsBuffer = scala.collection.concurrent.TrieMap.empty[Int, AlertManagerAlert]
  private val republisher = republisherFn(amPublisher, config.publishInterval, this)

  override def publish(alert: Alert): JTry[Alert] = {
    //when INFO is sent this means the alert has been resolved
    val amAlert = if (alert.level == AlertLevel.INFO) {
      val endDateTime = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now())
      raisedAlertsBuffer
        .remove(alert.alertId)
        .map(_.copy(endsAt = Some(endDateTime)))

    } else {
      val convertedAlert = alert.toAMAlert(config)
      raisedAlertsBuffer += alert.alertId -> convertedAlert
      Some(convertedAlert)
    }

    amAlert
      .map(amPublisher.publish)
      .getOrElse(Success(()))
      .map(_ => alert)
      .asJava
  }

  override def close(): Unit = {
    republisher.close()
    amPublisher.close()
  }

  override def getAlerts: Iterable[AlertManagerAlert] = raisedAlertsBuffer.values
  override def displayedInformation(): util.Map[String, String] = {
    import scala.jdk.CollectionConverters._
    Map(
      Config.Endpoints -> config.endpoints.mkString(","),
      "Lenses URL" -> config.generatorUrl,
      "Publish Interval" -> s"${config.publishInterval} ms",
      Config.SSL -> config.httpConfig.ssl.toString,
      "HTTP connection timeout" -> s"${config.httpConfig.connectTimeout} ms",
      "HTTP request timeout" -> s"${config.httpConfig.requestTimeout} ms",
      "HTTP socket timeout" -> s"${config.httpConfig.socketTimeout} ms"
    ).asJava
  }
}
