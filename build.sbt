// Repository settings
ThisBuild / organization := "io.lenses"
ThisBuild / organizationName := "Lenses.io"
ThisBuild / organizationHomepage := Some(url("http://lenses.io/"))
ThisBuild / developers := List(
  Developer(
    "michalludwinowicz",
    "Michal Ludwinowicz",
    "michal.ludwinowicz@lenses.io",
    null
  ),
  Developer(
    "stheppi",
    "Stefan Bocutiu",
    "stefan@lenses.io",
    null
  ),
  Developer(
    "skennedy",
    "Stephen Kennedy",
    "stephen.kennedy@lenses.io",
    null
  )
)
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("http://github.com/landoop/lenses-alerts-plugin"))

// Build settings
ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / javacOptions ++= Seq(
  "-source", "1.8",
  "-target", "1.8"
)
ThisBuild / scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint:infer-any",
  "-Xfatal-warnings",
  "-feature",
  "-Ywarn-dead-code",
  "-Ywarn-unused-import",
  "-Xmax-classfile-name", "128",
  "-Ypartial-unification",
  "-language:higherKinds",
  "-Ybackend-parallelism", "8"
)
ThisBuild / ghreleaseNotes := identity
ThisBuild / pomIncludeRepository := { _ => false }

// Dependencies
val scalaJava8Compat = "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.0"
val sl4fj = "org.slf4j" % "slf4j-api" % "1.7.25"
val jslack = "com.github.seratch" % "jslack" % "1.0.26"
val httpclient = "org.apache.httpcomponents" % "httpclient" % "4.5.6"
val circeParser = "io.circe" %% "circe-parser" % "0.11.1"
val circeGeneric = "io.circe" %% "circe-generic" % "0.11.1"
val circeGenericExtras = "io.circe" %% "circe-generic-extras" % "0.11.1"
val wiremock = "com.github.tomakehurst" % "wiremock" % "2.23.2"
val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"

// Root project
lazy val root = (project in file("."))
  .disablePlugins(AssemblyPlugin)
  .disablePlugins(SbtGithubReleasePlugin)
  .aggregate(alertsPluginApi, slackAlertsPlugin, alertManagerPlugin)

Compile / packageBin := { file("") }
Compile / packageSrc := { file("") }
Compile / packageDoc := { file("") }

lazy val alertsPluginApi = (project in file("lenses-alerts-plugin-api"))
  .disablePlugins(AssemblyPlugin)
  .settings(
    description := "Lenses.io Alerts Plugin API",
    libraryDependencies += scalaJava8Compat,
    libraryDependencies += scalaTest % Test,
    ghreleaseNotes := identity
  )

lazy val slackAlertsPlugin = (project in file("lenses-slack-alerts-plugin"))
  .dependsOn(alertsPluginApi)
  .settings(
    description := "Lenses.io Slack Alerts Plugin",
    libraryDependencies += sl4fj % Provided,
    libraryDependencies += jslack,
    libraryDependencies += logbackClassic % Test,
    libraryDependencies += scalaTest % Test,
    assembly / assemblyJarName := s"${name.value}-standalone-${version.value}.jar",
    ghreleaseAssets += (assembly / assemblyOutputPath).value,
    ghreleaseNotes := identity
  )

lazy val alertManagerPlugin = (project in file("lenses-alertmanager-plugin"))
  .dependsOn(alertsPluginApi)
  .settings(
    description := "Lenses.io Prometheus Alert-Manager Plugin",
    libraryDependencies += sl4fj % Provided,
    libraryDependencies += httpclient % Provided,
    libraryDependencies += circeParser % Provided,
    libraryDependencies += circeGeneric % Provided,
    libraryDependencies += circeGenericExtras % Provided,
    libraryDependencies += logbackClassic % Test,
    libraryDependencies += scalaTest % Test,
    libraryDependencies += wiremock % Test,
    assembly / assemblyJarName := s"${name.value}-standalone-${version.value}.jar",
    ghreleaseAssets += (assembly / assemblyOutputPath).value,
    ghreleaseNotes := identity
  )
