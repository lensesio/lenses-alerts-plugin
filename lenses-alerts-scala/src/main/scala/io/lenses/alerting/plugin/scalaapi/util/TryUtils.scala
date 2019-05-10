/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.scalaapi.util

import scala.util.{Failure, Success}
import io.lenses.alerting.plugin.javaapi.util.{Failure => JFailure, Success => JSuccess, Try => JTry}

object TryUtils {
  implicit class TryOps[T](val t: JTry[T]) extends AnyVal {
    def asScala = t match {
      case s: JSuccess[T] => Success(s.value)
      case f: JFailure[T] => Failure(f.exception)
    }
  }
}
