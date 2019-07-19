package io.lenses.alerts.plugin.slack

/**
  * The configuration required for communicating with Slack
  *
  * @param webhookUrl  - Slack Web Hook URL
  * @param userName - The user for the messages posted on the channel
  * @param channel  - The channel to send the alert notifications
  * @param iconUrl  - The icon used for the messages on slack
  */
case class SlackConfig(webhookUrl: String,
                       userName: String,
                       channel: String,
                       iconUrl: Option[String])