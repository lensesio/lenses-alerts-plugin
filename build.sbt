ThisBuild / organization     := "io.lenses"
ThisBuild / organizationName := "Lenses"
// Uncomment when you're ready to start building 1.0.0-...-SNAPSHOT versions.
// ThisBuild / gitVersioningSnapshotLowerBound := "1.0.0"

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

val sl4fj = "org.slf4j" % "slf4j-api" % "1.7.25"
val httpclient = "org.apache.httpcomponents" % "httpclient" % "4.5.6"
val lensesAlerts = "io.lenses" %% "lenses-alerts-scala" % "5.0.0"
val circeParser = "io.circe" %% "circe-parser" % "0.11.1"
val circeGeneric = "io.circe" %% "circe-generic" % "0.11.1"
val circeGenericExtras = "io.circe" %% "circe-generic-extras" % "0.11.1"
val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
val pegdown = "org.pegdown" % "pegdown" % "1.6.0"
val wiremock = "com.github.tomakehurst" % "wiremock" % "2.23.2"

lazy val root = (project in file("."))
  .settings(
    name := "lenses-alertmanager-plugin",
    libraryDependencies += sl4fj % Provided,
    libraryDependencies += httpclient % Provided,
    libraryDependencies += lensesAlerts % Provided,
    libraryDependencies += circeParser % Provided,
    libraryDependencies += circeGeneric % Provided,
    libraryDependencies += circeGenericExtras % Provided,
    libraryDependencies += logbackClassic % Test,
    libraryDependencies += scalaTest % Test,
    libraryDependencies += pegdown % Test,
    libraryDependencies += wiremock % Test,
    ghreleaseAssets := List((assembly / assemblyOutputPath).value)

  )

