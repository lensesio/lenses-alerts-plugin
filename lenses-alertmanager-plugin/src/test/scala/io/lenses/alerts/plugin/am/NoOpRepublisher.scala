package io.lenses.alerts.plugin.am

object NoOpRepublisher extends Republisher {
  override def publisher: Publisher = NoOpPublisher
  override def close(): Unit = {}
}
