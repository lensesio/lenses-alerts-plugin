[![Build Status](https://travis-ci.org/Landoop/lenses-alerts-plugin.svg?branch=master)](https://travis-ci.org/Landoop/lenses-alerts-plugin)
[<img src="https://img.shields.io/maven-central/v/io.lenses/lenses-alerts-plugin.svg?label=latest%20release%20"/>](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lenses-alerts-plugin%22)


# lenses-alerts-plugin
Defines interface for plugable lenses alert services integration

# Build

This project uses Gradle build system. To compile and run the tests execute


```bash
    gradle clean test
```

In order to release run once `gradle.properties` has been updated with the ossrh and signing settings

```bash
    gradle clean build signArchives uploadArchives closeRepository releaseRepository
```
    


