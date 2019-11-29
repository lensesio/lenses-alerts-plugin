package io.lenses.alerts.plugin.cloudwatch

import io.lenses.alerting.plugin.javaapi.util.{Failure => JFailure, Success => JSuccess, Try => JTry}

import scala.util.{Failure, Success, Try}

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
