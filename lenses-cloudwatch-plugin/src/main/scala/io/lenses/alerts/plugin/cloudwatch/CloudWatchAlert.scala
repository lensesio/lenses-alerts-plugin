package io.lenses.alerts.plugin.cloudwatch


import java.time.format.DateTimeFormatter
import java.time.ZonedDateTime

import io.lenses.alerting.plugin.Alert
import io.lenses.alerting.plugin.scalaapi.{Alert => ScalaAlert}

import scala.compat.java8.OptionConverters._

/**
 * Encapsulates the information for an event in Kafka when something is not running at 100%
 *
 * @param labels      - A list of key value pairs. It will contain at least severity.
 * @param annotations - A list of key value pairs. The keys will contain: summary, source, and docs keys.
 * @param startsAt    - The time in ISO format of when the alert starts
 * @param endsAt      - When the alert finishes
 * @param sourceName  - The source name of the creator of this alert
 * @param alertId     - A unique identifier for the setting corresponding to this alert
 */
case class CloudWatchAlert(labels: Map[String, String],
                           annotations: Map[String, String],
                           startsAt: String,
                           endsAt: Option[String],
                           sourceName: String,
                           alertId: Int)

object CloudWatchAlert {

  import io.circe._
  import io.circe.generic.semiauto._

  implicit val AMAlertEncoder: Encoder.AsObject[CloudWatchAlert] = deriveEncoder[CloudWatchAlert]

  implicit class AlertExtension(val alert: Alert) extends AnyVal {
    def toCWAlert: CloudWatchAlert = {
      val docs = alert.docs.asScala
      CloudWatchAlert(
        Map(
          "category" -> alert.category,
          "severity" -> alert.level.toString,
          "instance" -> alert.instance
        ),
        docs.foldLeft {
          Map(
            "source" -> "Lenses",
            "summary" -> alert.summary
          )
        } { case (map, d) => map + ("docs" -> d) },
        DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()),
        None,
        "http://lenses",
        alert.alertId
      )
    }
  }

  implicit class ScalaAlertExtension(val alert: ScalaAlert) extends AnyVal {
    def toCWAlert: CloudWatchAlert = {
      CloudWatchAlert(
        Map(
          "category" -> alert.category,
          "severity" -> alert.level.toString,
          "instance" -> alert.instance
        ),
        alert.docs.foldLeft {
          Map(
            "source" -> "Lenses",
            "summary" -> alert.summary
          )
        } { case (map, d) => map + ("docs" -> d) },
        DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()),
        None,
        "http://lenses",
        alert.alertId
      )
    }
  }

}
