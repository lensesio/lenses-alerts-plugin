/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.scalaapi

import io.lenses.alerting.plugin.javaapi.{AlertingPlugin => JAlertingPlugin}
import io.lenses.alerting.plugin.scalaapi.util.TryUtils._

import scala.util.Try
import scala.collection.JavaConverters._

/**
  * simple alerting plugin wrapper
  */
class AlertingPlugin(underlying: JAlertingPlugin) {

  def init(config: Map[String, String]): Try[AlertingService] = for {
    init    <- Try { underlying.init(config.asJava) }
    service <- init.asScala
  } yield new AlertingService(service)
}

object AlertingPlugin {
  def apply(underlying: JAlertingPlugin): AlertingPlugin = new AlertingPlugin(underlying)
}