[![Build Status](https://travis-ci.org/Landoop/lenses-alerts-plugin.svg?branch=master)](https://travis-ci.org/Landoop/lenses-alerts-plugin)
[<img src="https://img.shields.io/maven-central/v/io.lenses/lenses-alerts-plugin-api_2.12.svg?label=latest%20release%20"/>](https://search.maven.org/search?q=lenses-alerts-plugin-api_2.12)


# lenses-alerts-plugin

Defines interface for pluggable lenses alert services integration, along with
some officially supported implementations.

## Modules

- [`lenses-alerts-plugin-api`](./lenses-alerts-plugin-api)
  - defines the API for an alerts plugin
- [`lenses-slack-alerts-plugin`](./lenses-slack-alerts-plugin)
  - defines an implementation for Slack integration
- [`lenses-alertmanager-plugin`](./lenses-alertmanager-plugin)
  - defines an implementation for Prometheus Alertmanager
- [`lenses-cloudwatch-plugin`](./lenses-cloudwatch-plugin)
  - defines an implementation for CloudWatch Events
  
All modules are published to Maven central. In addition, standalone JARs of 
each of plugin integration are available to download from Github releases, ready
to drop straight into a Lenses installation.

# Build

This project uses sbt. To compile and run the tests execute


```bash
sbt test
```

# Release

Initial setup:

- create `$HOME/.sbt/1.0/sonatype.sbt` with Sonatype account information:
```
credentials += Credentials("Sonatype Nexus Repository Manager",
                           "oss.sonatype.org",
                           "YOUR_SONATYPE_USERNAME",
                           "YOUR_SONATYPE_PASSWORD")
```

- create `$HOME/.github`, with an OAuth token created as per these [instructions](https://github.com/ohnosequences/sbt-github-release/tree/master#credentials):
```
oauth = YOUR_OAUTH_TOKEN
```

Create and publish the release:

- tag the release:
```
git tag vX.Y.Z
git push origin vX.Y.Z
```

- publish libraries to nexus
```
sbt publishSigned sonatypeRelease
```

- publish standalone jars to github
```
sbt assembly githubRelease
```
