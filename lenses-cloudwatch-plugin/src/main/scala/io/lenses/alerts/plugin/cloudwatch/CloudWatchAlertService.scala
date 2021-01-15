package io.lenses.alerts.plugin.cloudwatch

import java.util

import io.circe.syntax._
import io.lenses.alerting.plugin.Alert
import io.lenses.alerting.plugin.javaapi.AlertingService
import io.lenses.alerting.plugin.javaapi.util.{Try => JTry}
import io.lenses.alerts.plugin.cloudwatch.CloudWatchAlert._
import io.lenses.alerts.plugin.cloudwatch.TryUtils._
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.cloudwatchevents.CloudWatchEventsClient
import software.amazon.awssdk.services.cloudwatchevents.model.{PutEventsRequest, PutEventsRequestEntry}

import scala.util.{Failure, Success, Try}


class CloudWatchAlertService(override val name: String,
                             override val description: String,
                             config: CloudWatchConfig) extends AlertingService with Metadata {
  private val DetailType = "lensesAlerts"

  private val cloudWatchClient: CloudWatchEventsClient = {
    val credsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create(config.accessKey, config.accessSecretKey))
    val builder = CloudWatchEventsClient.builder().credentialsProvider(credsProvider)
    config.region.fold(builder)(r => builder.region(Region.of(r))).build()
  }

  override def publish(alert: Alert): JTry[Alert] = {
    Try {
      val convertedAlert = alert.toCWAlert

      val eventRequest = PutEventsRequestEntry.builder()
        .detail(convertedAlert.asJson.noSpaces)
        .detailType(DetailType)
        .source(config.source)
        .build()

      val request = PutEventsRequest.builder
        .entries(eventRequest)
        .build()

      cloudWatchClient.putEvents(request)
    }.flatMap { resp =>
      if (resp.failedEntryCount() == 0) {
        Success(alert)
      } else {
        Failure(new Exception(s"CloudWatch put event failed with these failed entries: ${resp.entries()}"))
      }
    }.asJava
  }

  override def displayedInformation(): util.Map[String, String] = {
    import scala.jdk.CollectionConverters._
    Map(
      "Source" -> config.source
    ).asJava
  }
}
