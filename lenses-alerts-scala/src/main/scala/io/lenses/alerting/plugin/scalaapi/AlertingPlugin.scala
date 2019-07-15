/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.scalaapi

import java.util.Optional

import io.lenses.alerting.plugin.javaapi.{AlertingPlugin => JAlertingPlugin}
import io.lenses.alerting.plugin.scalaapi.util.TryUtils._

import scala.collection.JavaConverters._
import scala.util.Try

/**
  * simple alerting plugin wrapper
  */
class AlertingPlugin(underlying: JAlertingPlugin) {

  def init(config: Map[String, String], nameOverride: Option[String], descriptionOverride: Option[String]): Try[AlertingService] = for {
    init <- Try {
      underlying.init(config.asJava, Optional.ofNullable(nameOverride.orNull), Optional.ofNullable(descriptionOverride.orNull))
    }
    service <- init.asScala
  } yield new AlertingService(service)

  def name: String = underlying.name()

  def description: String = underlying.description()

  def configKeys: Seq[ConfigEntry] = underlying.configKeys().asScala.map { c => ConfigEntry(c.getKey, c.getDescription) }.toList
}

object AlertingPlugin {
  def apply(underlying: JAlertingPlugin): AlertingPlugin = new AlertingPlugin(underlying)
}

case class ConfigEntry(key: String, desc: String)
