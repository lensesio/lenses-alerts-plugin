package io.lenses.alerting.plugin.scalaapi.util

import io.lenses.alerting.plugin.util.{Failure => JFailure, Success => JSuccess, Try => JTry}

import scala.util.{Failure, Success}

object TryUtils {
  implicit class TryOps[T](val t: JTry[T]) extends AnyVal {
    def asScala = t match {
      case s: JSuccess[T] => Success(s.value)
      case f: JFailure[T] => Failure(f.exception)
    }
  }
}
