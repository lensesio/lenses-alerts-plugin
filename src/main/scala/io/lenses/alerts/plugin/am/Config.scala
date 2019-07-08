package io.lenses.alerts.plugin.am

import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.ssl.SSLContextBuilder
import org.apache.http.ssl.TrustStrategy

import scala.util.Try

/**
  * Contains settings for pushing alerts to Alert-Manager
  *
  * @param endpoints         - A list of Alert-Manager HTTP endpoints
  * @param sourceEnvironment - The defaul name of reporter to alert manager
  * @param generatorUrl      - A URL to identify the reporter
  * @param publishInterval   - The interval to push to Alert-Manager raised alerts. Alert Manager requires to keep pushing the alert otherwise is considered resolved
  * @param httpConfig        - All the configs related to HTTP requests
  */
case class Config(endpoints: List[String],
                  sourceEnvironment: String,
                  generatorUrl: String,
                  publishInterval: Long,
                  httpConfig: HttpConfig)

case class HttpConfig(connectTimeout: Int = 2000,
                      requestTimeout: Int = 2000,
                      socketTimeout: Int = 2000,
                      redirects: Boolean = true,
                      ssl: Boolean = false) {
  def buildHttpClient(): CloseableHttpClient = {
    if (ssl) {
      val acceptingTrustStrategy: TrustStrategy = (_, _) => true
      val sslContext = new SSLContextBuilder()
        .loadTrustMaterial(null, acceptingTrustStrategy)
        .build()

      HttpClientBuilder.create()
        .setSSLContext(sslContext)
        .setSSLHostnameVerifier(new NoopHostnameVerifier)
        .build()
    } else {
      HttpClientBuilder.create().build()
    }
  }
}

object Config {
  val Endpoints = "endpoints"
  val Source = "source"
  val GeneratorUrl = "generator.url"

  val SSL = "ssl"
  val SSLDefault = false

  val PublishInterval = "publish.interval"
  val PublishIntervalDefault: Long = 5 * 60 * 1000 //5 minutes

  val HttpConnectTimeout = "http.timeout.connect"
  val HttpConnectTimeoutDefault = 5000

  val HttpRequestTimeout = "http.timeout.request"
  val HttpRequestTimeoutDefault = 15000

  val HttpSocketTimeout = "http.timeout.socket"
  val HttpSocketTimeoutDefault = 5000

  private def getOrError(key: String, map: Map[String, String]): String = {
    map.getOrElse(key, throw new IllegalArgumentException(s"Invalid configuration for Alert Manager plugin [$key]"))
  }

  private def getOptionalKey[T](key: String, map: Map[String, String], default: T)(thunk: String => T) = {
    map.get(key).map { v =>
      Try(thunk(v)).getOrElse {
        throw new IllegalArgumentException(s"Invalid configuration. Value [$v] is not a valid configuration for Alert-Manager plugin key [$key]")
      }
    }.getOrElse(default)
  }

  def from(map: Map[String, String]): Config = {
    val endpoints = getOrError(Endpoints, map).split(',').map(_.trim).filter(_.nonEmpty).toList
    if (endpoints.isEmpty) {
      throw new IllegalArgumentException(s"Invalid configuration for Alert Manager plugin [$Endpoints]. No values was provided.")
    }
    val source = getOrError(Source, map)
    val generatorUrl = getOrError(GeneratorUrl, map)
    val publishInterval = getOptionalKey(PublishInterval, map, PublishIntervalDefault)(_.toLong)

    Config(endpoints,
      source,
      generatorUrl,
      publishInterval,
      httpConfigFrom(map))
  }

  def httpConfigFrom(map: Map[String, String]): HttpConfig = {
    val connection = getOptionalKey(HttpConnectTimeout, map, HttpConnectTimeoutDefault)(_.toInt)
    val request = getOptionalKey(HttpRequestTimeout, map, HttpRequestTimeoutDefault)(_.toInt)
    val socket = getOptionalKey(HttpSocketTimeout, map, HttpSocketTimeoutDefault)(_.toInt)

    val ssl = getOptionalKey(SSL, map, SSLDefault)(_.toBoolean)

    HttpConfig(connection, request, socket, true, ssl)
  }
}