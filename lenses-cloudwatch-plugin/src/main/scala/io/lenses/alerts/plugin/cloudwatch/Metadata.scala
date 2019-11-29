package io.lenses.alerts.plugin.cloudwatch

trait Metadata {
  def name(): String = "CloudWatch";
  def description(): String = "Publishes all the alerts to CloudWatch events."
}
