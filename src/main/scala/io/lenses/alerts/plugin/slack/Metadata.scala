package io.lenses.alerts.plugin.slack

trait Metadata {
  def name(): String = "Slack"
  def description(): String = "Publishes all the raised alerts to a Slack channel."
}
