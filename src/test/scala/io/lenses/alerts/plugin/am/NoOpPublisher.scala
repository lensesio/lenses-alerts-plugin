package io.lenses.alerts.plugin.am

import scala.util.Success
import scala.util.Try

object NoOpPublisher extends Publisher {
  override def publish(alert: AlertManagerAlert): Try[Unit] = Success(())
  override def close(): Unit = {}
}
