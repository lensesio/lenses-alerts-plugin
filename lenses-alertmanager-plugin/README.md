## Lenses Alert integration for Prometheus Alert-Manager

This is a Lenses plugin allowing to push Lenses raised alerts to [Prometheus Alert Manager](https://prometheus.io/docs/alerting/alertmanager/).

### Configuration

In order to enable the plugin, the following settings has to be set in Lenses:

```
lenses.alerting.plugins = [
  {
    class=io.lenses.alerts.plugin.am.AlertManagerPlugin
    config={
      endpoints="http://AM1:PORT1,http://AM2:PORT2"
      source=PROD
      generator-url="http://lenses_prod:port1"
    }
  }
]
```

|Configuration         | Type    | Description                                                                                 |
|----------------------|---------|---------------------------------------------------------------------------------------------|
| endpoints            | String  | Comma separated list of Alert Manager endpoints.                                            |
| source               | String  | The Lenses raising the alert. For example PROD.                                             |
| generator-url        | String  | A URL to identify the source. For example http://lenses_prod:port1                          |
| ssl                  | boolean | (Optional, false). If true it enables SSL.                                                  |
| publish-interval     | int     | (Optional, 300000). The interval in milliseconds to send the alerts raised to Alert manager.|
| http-connect-timeout | int     | (Optional, 5000). Time in milliseconds which determines the timeout in milliseconds until a connection is established.|
| http-request-timeout | int     | (Optional, 5000).  The timeout in milliseconds used when requesting a connection from the connection manager.|
| http-socket-timeout  | int     | (Optional, 5000).  Defines the socket timeout (SO_TIMEOUT) in milliseconds, which is the timeout for waiting for data or, put differently, a maximum period inactivity between two consecutive data packets).|

### How it works

Once an alert is raised, in order to avoid having the Alert Manager clearing it, it has to be re-send.
The `publish-interval` controls how often this happens. Once the alert has been resolved, the alert is posted to Alert Manager
with the end timestamp stamp and from that point onwards the alert is not pushed to Alert Manager, unless it is raised again.
