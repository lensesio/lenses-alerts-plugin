/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.scalaapi

import java.util.{Map => JMap}
import java.util.Optional
import scala.collection.JavaConverters._
import io.lenses.alerting.plugin

final case class Alert(labels: JMap[String, String],  annotations: JMap[String, String],  startsAt: String,
                 endsAt: Optional[String],  generatorURL: String,  alertId: Int) extends plugin.Alert

object Alert {
  def apply(labels: Map[String, String], annotations: Map[String, String], startsAt: String,
            endsAt: Option[String], generatorURL: String, alertId: Int): Alert =
    new Alert(labels.asJava, annotations.asJava, startsAt,
      endsAt.fold(Optional.empty[String]())(Optional.of),generatorURL, alertId)
}
