package io.lenses.alerting.plugin.scalaapi

import io.lenses.alerting.plugin.{Alert => JAlert}

import scala.collection.JavaConverters._

object AlertConversion {

  implicit class AlertExtension(val alert: JAlert) extends AnyVal {
    def asScala: Alert = {
      val docs: Option[String] = if (alert.docs.isPresent) Some(alert.docs.get) else None
      Alert(
        alert.level,
        alert.category,
        alert.tags.asScala.toList,
        alert.instance,
        alert.summary,
        docs,
        alert.timestamp,
        alert.alertId,
        alert.labels.asScala.toMap)
    }
  }

}
