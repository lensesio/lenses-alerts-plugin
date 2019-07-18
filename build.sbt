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
ghreleaseNotes := identity
ThisBuild / pomIncludeRepository := { _ => false }

val scalaJava8Compat = "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.0"
val sl4fj = "org.slf4j" % "slf4j-api" % "1.7.25"
val jslack = "com.github.seratch" % "jslack" % "1.0.26"
val lensesAlerts = "io.lenses" %% "lenses-alerts-scala" % "5.0.0"
val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
val pegdown = "org.pegdown" % "pegdown" % "1.1.0"

lazy val alertsPluginApi = (project in file("lenses-alerts-plugin-api"))
  .settings(
    name := "lenses-alerts-plugin-api",
    description := "Lenses.io Alerts Plugin API",
    libraryDependencies += scalaJava8Compat,
    libraryDependencies += scalaTest % Test,
  )

