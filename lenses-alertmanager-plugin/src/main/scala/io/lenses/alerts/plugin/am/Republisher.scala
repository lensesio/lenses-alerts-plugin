package io.lenses.alerts.plugin.am

import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.ExecutorService

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContextExecutorService
import scala.concurrent.Future
import scala.util.Try

/**
  * Alert-Manager needs to get the alert until it's fixed.
  * If it is not published it will mark the alert as fixed
  */
trait Republisher extends AutoCloseable {
  def publisher: Publisher
}

object Republisher {
  def async(publisher: Publisher, interval: Long, alerts: AlertsRaised) = AsyncRepublisher(publisher, interval, alerts)
}

final case class AsyncRepublisher(publisher: Publisher,
                                  interval: Long,
                                  alertsRaised: AlertsRaised)
  extends Republisher {

  private val pool: ExecutorService = Executors.newFixedThreadPool(1)
  private implicit val ec: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(pool)
  private val isRunning = new AtomicBoolean(true)

  private val publisherFuture = Future {
    try {
      while (isRunning.get()) {
        Thread.sleep(interval)
        alertsRaised.getAlerts
          .foreach(alert => Try(publisher.publish(alert)))
      }
    } catch {
      case _: InterruptedException =>
    }
  }

  override def close(): Unit = {
    isRunning.set(false)
    pool.shutdownNow()
  }
}

