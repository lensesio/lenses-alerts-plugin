package io.lenses.alerts.plugin.am

import io.lenses.alerting.plugin.javaapi.util.{Try => JTry}
import io.lenses.alerting.plugin.javaapi.util.{Failure => JFailure}
import io.lenses.alerting.plugin.javaapi.util.{Success => JSuccess}

import scala.util.Failure
import scala.util.Success
import scala.util.Try

object TryUtils {

  implicit class TryExtension[T](val t: Try[T]) extends AnyVal {
    def asJava: JTry[T] = {
      t match {
        case Success(value) => new JSuccess[T](value)
        case Failure(exception) => new JFailure[T](exception)
      }
    }
  }

}
