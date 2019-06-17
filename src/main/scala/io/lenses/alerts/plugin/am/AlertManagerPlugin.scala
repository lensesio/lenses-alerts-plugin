package io.lenses.alerts.plugin.am

import java.util

import io.lenses.alerting.plugin.javaapi.AlertingPlugin
import io.lenses.alerting.plugin.javaapi.util.{Try => JTry}
import io.lenses.alerting.plugin.javaapi.AlertingService
import io.lenses.alerts.plugin.am.TryUtils._

import scala.collection.JavaConverters._
import scala.util.Try

class AlertManagerPlugin extends AlertingPlugin {
  override def init(config: util.Map[String, String]): JTry[AlertingService] = Try {
    val amConfig = Config.from(config.asScala.toMap)
    val publisher = new HttpPublisher(amConfig)
    val as: AlertingService = new AlertManagerService(amConfig, publisher)
    as
  }.asJava
}
