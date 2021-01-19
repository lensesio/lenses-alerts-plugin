package io.lenses.alerting.plugin.mail

import java.util.Properties

case class MailAlertConfig(senderAddress: String,
                           emailAddresses: String,
                           user: String,
                           passwd: String,
                           mailProps: Properties)
