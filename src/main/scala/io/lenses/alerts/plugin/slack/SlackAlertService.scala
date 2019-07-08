package io.lenses.alerts.plugin.slack

import java.util

import com.github.seratch.jslack.Slack
import com.github.seratch.jslack.api.model.Attachment
import com.github.seratch.jslack.api.model.Field
import com.github.seratch.jslack.api.webhook.Payload
import io.lenses.alerting.plugin.javaapi.AlertingService
import io.lenses.alerting.plugin.Alert
import io.lenses.alerting.plugin.AlertLevel
import io.lenses.alerting.plugin.javaapi.util.{Try => JTry}
import io.lenses.alerts.plugin.slack.TryUtils._

import scala.collection.JavaConverters._
import scala.util.Try

class SlackAlertService(config: SlackConfig) extends AlertingService with Metadata {
  private val CriticalColor = "#BF350C"
  private val HighColor = "#F35A00"
  private val MediumColor = "#FF8043"
  private val LowColour = "#FCDC0D"
  private val InfoColor = "#0072CF"

  private val slack: Slack = Slack.getInstance
  private val payload = Payload.builder.channel(config.channel).username(config.userName).build()

  private val alertLevelField = Field.builder().title("Level").value("INFO").build()
  private val categoryField = Field.builder().title("Category").value("").build()

  private val attachment = Attachment.builder()
    .color(InfoColor)
    .fields(Seq(alertLevelField, categoryField).asJava)
    .footer("Lenses Notification").build()

  payload.setAttachments(Seq(attachment).asJava)
  config.iconUrl.foreach(payload.setIconUrl)

  override def publish(alert: Alert): JTry[Alert] = {
    Try {
      alertLevelField.setValue(alert.level.toString)
      categoryField.setValue(alert.category)
      attachment.setText(alert.summary)

      alert.level match {
        case AlertLevel.CRITICAL => attachment.setColor(CriticalColor)
        case AlertLevel.HIGH => attachment.setColor(HighColor)
        case AlertLevel.MEDIUM => attachment.setColor(CriticalColor)
        case AlertLevel.INFO => attachment.setColor(InfoColor)
        case _ => attachment.setColor(LowColour)
      }

      slack.send(config.webhookUrl, payload)
      alert
    }.asJava
  }
  override def displayedInformation(): util.Map[String, String] = {
    import scala.collection.JavaConverters._
    Map(
      "User Name" -> config.userName,
      "Channel" -> config.channel
    ).asJava
  }
}
