## Lenses Alert integration for Slack

This is a Lenses plugin allowing to push Lenses raised alerts to slack.

### Configuration

In order to enable the plugin, the following settings have to be set in Lenses:

```
    lenses.alerting.plugins = [
      {
        class=io.lenses.alerts.plugin.slack.SlackAlertsPlugin
        config={
          webhook-url="https://hooks.slack.com/services/SECRET/YYYYYYYYYYY/XXXXXXXX"
          username=Lenses™
          channel=lenses-slack
        }
      }
    ]
```

Apart from the above the plugin requires a few extra configurations as seen in the table below.


|Configuration   | Type   | Description                                                        |
|----------------|--------|--------------------------------------------------------------------|
| webhook-url    | String | Contains the Slack endpoint to send the alert to.                  |
| username       | String | The user name to appear in slack as the sender.                    |
| channel        | String | The name of the channel to send the alert to.                      |
| icon-url       | String | (Optional). The full path to an image to set for the slack message.|


## Build

In order to compile the code, you have to run the following command (Gradle needs to be installed and available
on your command prompt)

```
sbt assembly
```

To publish a release to github:
```
git tag vX.Y.Z
git push origin vX.Y.Z
export GITHUB_TOKEN=XXXXXX
sbt assembly githubRelease
```

The token should be created as per the [instructions](https://github.com/ohnosequences/sbt-github-release/tree/master#credentials) of the sbt-github-release plugin.


