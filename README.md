## Lenses Alert integration for Slack

This is a Lenses plugin allowing to push Lenses raised alerts to slack.

### Configuration

In order to enable the plugin, the following settings has to be set in Lenses:

```
    lenses.alerting.plugin.class=io.lenses.alerts.plugin.slack.SlackAlertsPlugin
```

Apart from the above the plugin requires a few extra configurations as seen in the table below.
All the configs have to be prefixed with `lenses.alerting.plugin.config`. For example:

```
    lenses.alerting.plugin.config.webhook.url=https://hooks.slack.com/services/SECRET/YYYYYYYYYYY/XXXXXXXX
```

|Configuration   | Type   | Description                                                        |
|----------------|--------|--------------------------------------------------------------------|
| webhook.url    | String | Contains the Slack endpoint to send the alert to.                  |
| username       | String | The user name to appear in slack as the sender.                    |
| channel        | String | The name of the channel to send the alert to.                      |
| icon.url       | String | (Optional). The full path to an image to set for the slack message.|



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

Then zip the files from build/libs and add it to Github. Only the two jars are required in the release,
since Lenses already brings in gson, okhttp, okio, sl4j.
