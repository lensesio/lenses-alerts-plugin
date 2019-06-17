## Lenses Alert integration for Prometheus Alert-Manager

This is a Lenses plugin allowing to push Lenses raised alerts to  Prometheus Alert Manager (https://prometheus.io/docs/alerting/alertmanager/).

### Configuration

In order to enable the plugin, the following settings has to be set in Lenses:

```
    lenses.alerting.plugin.class=io.lenses.alerts.plugin.am.AlertManagerPlugin
```

Apart from the above the plugin requires a few extra configurations as seen in the table below.
All the configs have to be prefixed with `lenses.alerting.plugin.config`. For example:

```
    lenses.alerting.plugin.config.endpoints=http://AM1:PORT1,http://AM2:PORT2
```

|Configuration         | Type    | Description                                                                                 |
|----------------------|---------|---------------------------------------------------------------------------------------------|
| endpoints            | String  | Comma separated list of Alert Manager endpoints.                                            |
| source               | String  | The Lenses raising the alert. For example PROD.                                             |
| generator.url        | String  | A URL to identify the source. For example http://lenses_prod:port1                          |
| ssl                  | boolean | (Optional, false). If true it enables SSL.                                                  |
| publish.interval     | int     | (Optional, 300000). The interval in milliseconds to send the alerts raised to Alert manager.|
| http.timeout.connect | boolean | (Optional, 5000). Time in milliseconds which determines the timeout in milliseconds until a connection is established.|
| http.timeout.request | boolean | (Optional, 5000).  The timeout in milliseconds used when requesting a connection from the connection manager.|
| http.timeout.socket  | boolean | (Optional, 5000).  Defines the socket timeout (SO_TIMEOUT) in milliseconds, which is the timeout for waiting for data or, put differently, a maximum period inactivity between two consecutive data packets).|

### How it works

Once an alert is raised, in order to avoid having the Alert Manager clearing it, it has to be re-send.
The `publish.interval` controls how often this happens. Once the alert has been resolved, the alert is posted to Alert Manger
with the end timestamp stamp and from that point onwards the alert is not pushed to Alert Manager, unless it is raised again.

## Build

In order to compile the code, you have to run the following command (Gradle needs to be installed and available
on your command prompt)

```
    gradle clean build
```

To build for release:

```
    ./build.sh
```

Then zip the files from build/libs and add it to Github.
Only the two jars are required in the release,
since Lenses already brings in gson, okhttp, okio, sl4j.
