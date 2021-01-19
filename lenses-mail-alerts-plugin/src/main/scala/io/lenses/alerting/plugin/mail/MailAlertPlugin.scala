package io.lenses.alerting.plugin.mail

import java.util
import java.util.Properties

import io.lenses.alerting.plugin.javaapi.util.{Try => JTry}
import io.lenses.alerting.plugin.javaapi.{AlertingPlugin, AlertingService, ConfigEntry}
import io.lenses.alerting.plugin.mail.MailAlertsPlugin._
import io.lenses.alerting.plugin.mail.TryUtils.TryExtension

import scala.collection.JavaConverters._
import scala.util.Try

class MailAlertPlugin extends AlertingPlugin with Metadata {

  override val name: String = "Mail"

  override val description: String = "Plugin to support pushing Lenses alerts as emails"

  override def init(config: util.Map[String, String]): JTry[AlertingService] = Try {
    val map = config.asScala
    def getOrError(key: String): String = {
      map.getOrElse(key, throw new IllegalArgumentException(s"Invalid configuration for Mail plugin'[$key]'"))
    }

    val senderAddress = getOrError(SENDER_ADDRESS)
    val emailAddresses = getOrError(EMAIL_ADDRESSES)
    val username = getOrError(USERNAME)
    val password = getOrError(PASSWORD)

    def buildMailProperties = {
      val smtpHost = getOrError(SMTP_HOST)
      val smtpPort = getOrError(SMTP_PORT)
      val smtpAuth = getOrError(SMTP_AUTH)
      val smtpStartTLS = getOrError(SMTP_STARTTLS)

      // https://javaee.github.io/javamail/docs/api/com/sun/mail/smtp/package-summary.html
      val props = new Properties()
      props.put("mail.smtp.host", smtpHost)
      props.put("mail.smtp.port", smtpPort)
      props.put("mail.smtp.auth", smtpAuth)
      props.put("mail.smtp.starttls.enable", smtpStartTLS)
      props
    }

    val props: Properties = buildMailProperties
    val as: AlertingService = new MailAlertService(name, description, MailAlertConfig(senderAddress, emailAddresses, username, password, props))
    as
  }.asJava

  override def configKeys(): util.List[ConfigEntry] =  {
    import scala.collection.JavaConverters._
    List(
      new ConfigEntry(SENDER_ADDRESS, "The sender mail address"),
      new ConfigEntry(EMAIL_ADDRESSES,"Comma separated address strings in RFC822 format"),
      new ConfigEntry(USERNAME,"Username"),
      new ConfigEntry(PASSWORD,"Password"),
      new ConfigEntry(SMTP_HOST ,"Host name of the SMTP mail server"),
      new ConfigEntry(SMTP_PORT,"Port of the SMTP mail server"),
      new ConfigEntry(SMTP_AUTH,"If true, attempt to authenticate the user using the AUTH command"),
      new ConfigEntry(SMTP_STARTTLS,"If true, enables the use of the STARTTLS command"),
    ).asJava
  }
}

object MailAlertsPlugin {
  val SENDER_ADDRESS = "from-address"
  val EMAIL_ADDRESSES = "email-addresses"
  val USERNAME = "username"
  val PASSWORD = "password"
  val SMTP_HOST = "smtp-host"
  val SMTP_PORT = "smtp-port"
  val SMTP_AUTH = "smtp-auth"
  val SMTP_STARTTLS = "smtp-starttls"
}
