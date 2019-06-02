/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.scalaapi

import java.util.Optional

import io.lenses.alerting.plugin.{Alert => JAlert}
import io.lenses.alerting.plugin.AlertLevel

import scala.collection.JavaConverters._

case class Alert(level: AlertLevel,
                 category: String,
                 tags: Seq[String],
                 instance: String,
                 summary: String,
                 docs: Option[String],
                 alertId: Int) {
  def asJava: JAlert = {
    new JAlert(level, category, tags.asJava, instance, summary, docs.map(d => Optional.of(d)).getOrElse(Optional.empty[String]()), alertId)
  }
}

object Alert {
  def apply(level: AlertLevel,
            category: String,
            tags: Seq[String],
            instance: String,
            summary: String,
            docs: Option[String],
            alertId: Int): JAlert = {
    new JAlert(level,
      category,
      tags.asJava,
      instance,
      summary,
      docs.map(Optional.of[String]).getOrElse(Optional.empty[String]()),
      alertId
    )
  }
}
