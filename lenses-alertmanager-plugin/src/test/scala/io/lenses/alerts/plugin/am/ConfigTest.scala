package io.lenses.alerts.plugin.am

import io.lenses.alerts.plugin.am.Config._
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ConfigTest extends FunSuite with Matchers {
  test("constructs the HttpConfig with the default http configs") {
    val expected = HttpConfig(HttpConnectTimeoutDefault,
      HttpRequestTimeoutDefault,
      HttpSocketTimeoutDefault,
      true,
      SSLDefault)
    Config.httpConfigFrom(Map.empty) shouldBe expected
  }

  test("constructs the HttpConfig with the values given") {
    val connection = 1000
    val request = 2000
    val socket = 3000
    val ssl = true
    val map = Map(
      HttpConnectTimeout -> connection.toString,
      HttpRequestTimeout -> request.toString,
      HttpSocketTimeout -> socket.toString,
      SSL -> ssl.toString
    )
    val expected = HttpConfig(connection, request, socket, true, ssl)
    Config.httpConfigFrom(map) shouldBe expected
  }

  test("raises an alert if the connection timeout is invalid") {
    val request = 2000
    val socket = 3000
    val ssl = true
    val map = Map(
      HttpConnectTimeout -> "abc",
      HttpRequestTimeout -> request.toString,
      HttpSocketTimeout -> socket.toString,
      SSL -> ssl.toString
    )
    intercept[IllegalArgumentException] {
      Config.httpConfigFrom(map)
    }
  }

  test("raises an alert if the socket timeout is invalid") {
    val connection = 1000
    val request = 2000
    val ssl = true
    val map = Map(
      HttpConnectTimeout -> connection.toString,
      HttpRequestTimeout -> request.toString,
      HttpSocketTimeout -> "abc",
      SSL -> ssl.toString
    )
    intercept[IllegalArgumentException] {
      Config.httpConfigFrom(map)
    }
  }

  test("raises an alert if the request timeout is invalid") {
    val connection = 1000
    val socket = 3000
    val ssl = true
    val map = Map(
      HttpConnectTimeout -> connection.toString,
      HttpRequestTimeout -> "1 3",
      HttpSocketTimeout -> socket.toString,
      SSL -> ssl.toString
    )
    intercept[IllegalArgumentException] {
      Config.httpConfigFrom(map)
    }
  }

  test("creates a AMConfig with the values provided") {
    val connection = 1000
    val request = 2000
    val socket = 3000
    val ssl = true
    val map = Map(
      Endpoints -> "http://abc:12455,http://xyz:12556, http://mno:22111",
      Source -> "PROD1",
      GeneratorUrl -> "http://lensesProd:33333",
      PublishInterval -> "30000000",
      HttpConnectTimeout -> connection.toString,
      HttpRequestTimeout -> request.toString,
      HttpSocketTimeout -> socket.toString,
      SSL -> ssl.toString
    )

    val expected = Config(
      List("http://abc:12455", "http://xyz:12556", "http://mno:22111"),
      "PROD1",
      "http://lensesProd:33333",
      30000000,
      HttpConfig(connection, request, socket, true, ssl)
    )

    Config.from(map) shouldBe expected
  }
}
