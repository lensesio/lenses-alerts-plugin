import sbt._
import sbtassembly.AssemblyPlugin.autoImport._
import sbtassembly.{MergeStrategy, PathList}

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
  ),
  Developer(
    "spirosoik",
    "Spiros Economakis",
    "spiros@lenses.io",
    null
  )
)
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("http://github.com/lensesio/lenses-alerts-plugin"))

// Build settings
ThisBuild / scalaVersion := "2.13.6"
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
  "-Ywarn-unused:imports",
  "-language:higherKinds",
  "-Ybackend-parallelism", "8"
)
ThisBuild / ghreleaseNotes := identity
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := sonatypePublishTo.value

// Dependencies
val scalaJava8Compat = "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.0"
val sl4fj = "org.slf4j" % "slf4j-api" % "1.7.30"
val jslack = "com.github.seratch" % "jslack" % "1.0.26"
val awsCloudWatchEvents = "software.amazon.awssdk" % "cloudwatchevents" % "2.16.48"
val httpclient = "org.apache.httpcomponents" % "httpclient" % "4.5.13"
val circeParser = "io.circe" %% "circe-parser" % "0.14.1"
val circeGeneric = "io.circe" %% "circe-generic" % "0.14.1"
val circeGenericExtras = "io.circe" %% "circe-generic-extras" % "0.14.1"
val wiremock = "com.github.tomakehurst" % "wiremock" % "2.23.2"
val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
val scalaTest = "org.scalatest" %% "scalatest" % "3.2.2"

// Root project
lazy val root = (project in file("."))
  .disablePlugins(AssemblyPlugin)
  .aggregate(alertsPluginApi, slackAlertsPlugin, alertManagerPlugin, cloudWatchAlertsPlugin)
  .settings(
    name := "lenses-alerts-plugin",
    ghreleaseRepoOrg := "lensesio",
    ghreleaseNotes := identity,
    ghreleaseAssets := List(
      (slackAlertsPlugin / assembly / assemblyOutputPath).value,
      (alertManagerPlugin / assembly / assemblyOutputPath).value,
      (cloudWatchAlertsPlugin / assembly / assemblyOutputPath).value,
    ),
    publish / skip := true
  )

lazy val alertsPluginApi = (project in file("lenses-alerts-plugin-api"))
  .disablePlugins(AssemblyPlugin)
  .disablePlugins(SbtGithubReleasePlugin)
  .settings(
    name := "lenses-alerts-plugin-api",
    description := "Lenses.io Alerts Plugin API",
    libraryDependencies += scalaJava8Compat,
    libraryDependencies += scalaTest % Test,
  )

lazy val slackAlertsPlugin = (project in file("lenses-slack-alerts-plugin"))
  .disablePlugins(SbtGithubReleasePlugin)
  .dependsOn(alertsPluginApi)
  .settings(
    name := "lenses-slack-alerts-plugin",
    description := "Lenses.io Slack Alerts Plugin",
    libraryDependencies += sl4fj % Provided,
    libraryDependencies += jslack,
    libraryDependencies += logbackClassic % Test,
    libraryDependencies += scalaTest % Test,
    assembly / assemblyJarName := s"${name.value}-standalone-${version.value}.jar",
  )

lazy val alertManagerPlugin = (project in file("lenses-alertmanager-plugin"))
  .disablePlugins(SbtGithubReleasePlugin)
  .dependsOn(alertsPluginApi)
  .settings(
    name := "lenses-alertmanager-plugin",
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
  )

lazy val cloudWatchAlertsPlugin = (project in file("lenses-cloudwatch-plugin"))
  .disablePlugins(SbtGithubReleasePlugin)
  .dependsOn(alertsPluginApi)
  .settings(
    name := "lenses-cloudwatch-plugin",
    description := "Lenses.io CloudWatch Alerts Plugin",
    libraryDependencies += sl4fj % Provided,
    libraryDependencies += circeParser % Provided,
    libraryDependencies += circeGeneric % Provided,
    libraryDependencies += circeGenericExtras % Provided,
    libraryDependencies += awsCloudWatchEvents,
    libraryDependencies += logbackClassic % Test,
    libraryDependencies += scalaTest % Test,
    assemblyMergeStrategy in assembly  :=  {
      case PathList(ps@_*) if Set(
        "codegen.config" ,
        "service-2.json" ,
        "waiters-2.json" ,
        "customization.config" ,
        "examples-1.json" ,
        "paginators-1.json",
        "module-info.class",
      ).contains(ps.last) =>
        MergeStrategy.discard
      case x@PathList("META-INF", path@_*) =>
        path map {
          _.toLowerCase
        } match {
          case "io.netty.versions.properties" :: Nil =>
            MergeStrategy.first
          case _ =>
            val oldStrategy = (assemblyMergeStrategy in assembly).value
            oldStrategy(x)
        }
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    },
    assembly / assemblyJarName := s"${name.value}-standalone-${version.value}.jar",
  )
