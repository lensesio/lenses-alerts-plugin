package io.lenses.alerts.plugin.am

trait AlertsRaised {
  def getAlerts: Iterable[AlertManagerAlert]
}
