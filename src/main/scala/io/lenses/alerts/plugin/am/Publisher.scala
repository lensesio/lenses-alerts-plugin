package io.lenses.alerts.plugin.am

import java.io.BufferedReader
import java.io.InputStreamReader

import io.circe.syntax._
import io.lenses.alerts.plugin.am.AlertManagerAlert._
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity

import scala.util.Failure
import scala.util.Try

trait Publisher extends AutoCloseable {
  def publish(alert: AlertManagerAlert): Try[Unit]
}

class HttpPublisher(config: Config) extends Publisher {
  private val ApiUrl = "/api/v1/alerts"

  private val requestConfig = RequestConfig.custom
    .setConnectTimeout(config.httpConfig.connectTimeout)
    .setConnectionRequestTimeout(config.httpConfig.requestTimeout)
    .setRedirectsEnabled(config.httpConfig.redirects)
    .setSocketTimeout(config.httpConfig.socketTimeout)
    .build()

  private val client = config.httpConfig.buildHttpClient()

  def publish(alert: AlertManagerAlert): Try[Unit] = {
    val entity = new StringEntity(List(alert).asJson.noSpaces)

    val start: Try[Unit] = Failure(new RuntimeException("Failed to publish to Alert-Manager"))

    config.endpoints.foldLeft(start) { case (t, endpoint) =>
      t.recoverWith { case _ =>
        Try(publishInternal(endpoint + ApiUrl, entity))
      }
    }
  }

  private def publishInternal(endpoint: String, entity: StringEntity): Unit = {
    val post = new HttpPost(endpoint)
    post.setEntity(entity)
    post.setHeader("Accept", "application/json")
    post.setHeader("Content-type", "application/json")
    post.setConfig(requestConfig)

    var response: CloseableHttpResponse = null
    try {
      response = client.execute(post)
      if (response.getStatusLine.getStatusCode != 200) {
        throw new RuntimeException(s"The HTTP call on [$endpoint] failed with. Return code is [${response.getStatusLine.getStatusCode}]- ${response.getStatusLine.getReasonPhrase}.")
      }
      Option(response.getEntity).foreach { entity =>
        val rd = new BufferedReader(new InputStreamReader(entity.getContent))
        try {
          while (rd.read() != -1) {}
        }
        finally {
          Try(rd.close())
        }
      }
    }
    finally {
      Try(Option(response).foreach(_.close()))
    }
  }

  override def close(): Unit = Try(client.close())
}
