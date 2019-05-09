package io.lenses.alerting.plugin.scalaapi

import java.util

import io.lenses.alerting.plugin.{AlertingPlugin => JAlertingPlugin}
import io.lenses.alerting.plugin.util.{Try => JTry}

import scala.util.Try

/**
  * simple alerting plugin wrapper giving Scala users a bit more type information
  */
class AlertingPlugin(alertingPlugin: JAlertingPlugin) {

  def init(config: util.Map[String, String]): Try[AlertingService] = for {
    init <- Try { alertingPlugin.init(config) }
  } yield x
//    Try { new AlertingService(alertingPlugin.init(config)) }
}
