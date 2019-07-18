package io.lenses.alerts.plugin.am

trait Metadata {
  def name(): String = "Alert Manager";
  def description(): String = "Publishes all the alerts to Alert Manager. Based on its configuration the alerts will be routed further."
}
