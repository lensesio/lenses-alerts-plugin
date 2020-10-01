package io.lenses.alerting.plugin.mail

trait Metadata {
  def name(): String = "Mail"
  def description(): String = "Send an email for each Lenses alert."
}
