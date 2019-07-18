package io.lenses.alerting.plugin.scalaapi

import io.lenses.alerting.plugin.javaapi.{AlertingPlugin => JAlertingPlugin}
import io.lenses.alerting.plugin.javaapi
import io.lenses.alerting.plugin.javaapi.util.Try
import io.lenses.alerting.plugin.javaapi.{ConfigEntry=>JConfigEntry}

import scala.collection.JavaConverters._

class TestBasePlugin(thunk: => Try[javaapi.AlertingService]) extends JAlertingPlugin {
  override def name(): String = "name"
  override def description(): String = "description"
  override def configKeys(): java.util.List[JConfigEntry] = List.empty.asJava
  override def init(config: java.util.Map[String, String]): Try[javaapi.AlertingService] = thunk
}
