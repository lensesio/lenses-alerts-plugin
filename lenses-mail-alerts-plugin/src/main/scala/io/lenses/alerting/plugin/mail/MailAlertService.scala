package io.lenses.alerting.plugin.mail

import java.sql.Timestamp
import java.util
import java.util.Date

import io.lenses.alerting.plugin.Alert
import io.lenses.alerting.plugin.javaapi.AlertingService
import io.lenses.alerting.plugin.javaapi.util.{Try => JTry}
import io.lenses.alerting.plugin.mail.TryUtils.TryExtension
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Authenticator, Message, PasswordAuthentication, Session, Transport}

import scala.util.Try

class MailAlertService(override val name: String,
                       override val description: String,
                       config: MailAlertConfig) extends AlertingService with Metadata {

  val session: Session = Session.getInstance(config.mailProps, new Authenticator() {
    override protected def getPasswordAuthentication = new PasswordAuthentication(config.user, config.passwd)
  })

  override def displayedInformation(): util.Map[String, String] = {
    import scala.collection.JavaConverters._
    Map("Email Addresses" -> config.emailAddresses).asJava
  }

  override def publish(alert: Alert): JTry[Alert] = {
    Try {
      val message = new MimeMessage(session)
      message.setFrom(new InternetAddress(config.senderAddress))
      message.setRecipients(Message.RecipientType.TO, config.emailAddresses)
      message.setSubject("Lenses Alert")
      message.setSentDate(new Date())
      message.setText(alertToText(alert))

      Transport.send(message)
      alert
    }.asJava
  }

  private def alertToText(alert: Alert): String = {
    val sb = new StringBuilder()
    sb.append("AlertId: ").append(alert.alertId).append("\n")
    sb.append("Category: ").append(alert.category).append("\n")
    sb.append("Level: ").append(alert.level).append("\n")
    sb.append("Timestamp: ").append(new Timestamp(alert.timestamp).toString).append("\n")
    sb.append("Labels: ").append(alert.labels).append("\n")
    sb.append("Tags: ").append(alert.tags).append("\n")
    sb.append("Summary: ").append(alert.summary).append("\n")
    alert.docs.ifPresent(docs => sb.append("Docs: ").append(docs).append("\n"))
    sb.toString()
  }
}
