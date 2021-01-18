/*
 * Copyright 2017-2019 Lenses.io Ltd
 */
package io.lenses.alerting.plugin.scalaapi.util

import io.lenses.alerting.plugin.javaapi.util.{Failure => JFailure, Success => JSuccess}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.util.{Failure, Success}

class TryUtilsSpec extends AnyWordSpec with Matchers {
  import TryUtils._

  "asScala" should {
    "convert failure to scala.util.Failure" in {
      val e = new Exception("boom!")

      new JFailure(e).asScala shouldBe Failure(e)
    }

    "convert success to scala.util.Success" in {
      val v = "hello"

      new JSuccess(v).asScala shouldBe Success(v)
    }
  }
}
