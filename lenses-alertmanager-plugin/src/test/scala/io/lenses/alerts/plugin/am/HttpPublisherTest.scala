package io.lenses.alerts.plugin.am

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import io.circe.syntax._
import io.lenses.alerting.plugin.AlertLevel
import io.lenses.alerting.plugin.scalaapi.Alert
import io.lenses.alerts.plugin.am.AlertManagerAlert._
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.util.Success

class HttpPublisherTest extends AnyFunSuite with Matchers with BeforeAndAfterAll {
  val config = Config(List("http://machine:12333"), "prod1", "http://lensesisgreat:42424", 10000, HttpConfig())

  test("pushes the alert to alert manager") {
    val Port = 42420
    val Host = "localhost"
    val wireMockServer = new WireMockServer(wireMockConfig().port(Port))
    wireMockServer.start()
    WireMock.configureFor(Host, Port)

    try {
      val url = "/api/v1/alerts"
      wireMockServer.addStubMapping(
        stubFor(post(urlEqualTo(url))
          .willReturn(
            aResponse()
              .withStatus(200)))
      )

      val alert = Alert(
        AlertLevel.CRITICAL,
        "infrastructure",
        List("tag1"),
        "prod1",
        "This is important",
        None,
        System.currentTimeMillis(),
        1,
        Map.empty).asJava.toAMAlert(config)

      val json = List(alert).asJson.noSpaces

      val publisher = new HttpPublisher(Config(List(s"http://localhost:$Port"), "dev", "http://lenses.io", 10000, HttpConfig()))
      publisher.publish(alert) shouldBe Success(())

      wireMockServer.verify(postRequestedFor(urlEqualTo(url))
        .withRequestBody(equalTo(json))
        .withHeader("Content-Type", equalTo("application/json")))
    }
    finally {
      wireMockServer.stop()
    }
  }

  test("if first endpoint fails it tries the second") {
    val Port = 42420
    val Host = "localhost"
    val wireMockServer = new WireMockServer(wireMockConfig().port(Port))
    wireMockServer.start()
    WireMock.configureFor(Host, Port)

    try {
      val url = "/api/v1/alerts"
      wireMockServer.addStubMapping(
        stubFor(post(urlEqualTo(url))
          .willReturn(
            aResponse()
              .withStatus(200)))
      )

      val alert = Alert(
        AlertLevel.CRITICAL,
        "infrastructure",
        List("tag1"),
        "prod1",
        "This is important",
        None,
        System.currentTimeMillis(),
        1,
        Map.empty).asJava.toAMAlert(config)

      val json = List(alert).asJson.noSpaces

      val publisher = new HttpPublisher(Config(List(s"http://localhost:11111", s"http://localhost:$Port"), "dev", "http://lenses.io", 10000, HttpConfig()))
      publisher.publish(alert) shouldBe Success(())

      wireMockServer.verify(postRequestedFor(urlEqualTo(url))
        .withRequestBody(equalTo(json))
        .withHeader("Content-Type", equalTo("application/json")))
    }
    finally {
      wireMockServer.stop()
    }
  }

  test("if the endpoint fails it returns a Failure") {

    val alert = Alert(
      AlertLevel.CRITICAL,
      "infrastructure",
      List("tag1"),
      "prod1",
      "This is important",
      None,
      System.currentTimeMillis(),
      1,
      Map.empty).asJava.toAMAlert(config)

    val json = List(alert).asJson.noSpaces

    val publisher = new HttpPublisher(Config(List(s"http://localhost:11111"), "dev", "http://lenses.io", 10000, HttpConfig()))
    publisher.publish(alert).isFailure shouldBe true
  }
}