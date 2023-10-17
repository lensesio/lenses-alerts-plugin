package io.lenses.alerts.plugin.cloudwatch

/**
  * The configuration required for communicating with CloudWatch
  *
  * @param accessKey       - The AWS access key of an IAM account
  * @param accessSecretKey - The AWS access secret key of an IAM account
  * @param source          - The source name which will be added in the CloudWatch event
  */
case class CloudWatchConfig(
  accessKey:       Option[String],
  accessSecretKey: Option[String],
  region:          Option[String],
  source:          String,
)
