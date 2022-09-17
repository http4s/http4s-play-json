ThisBuild / tlBaseVersion := "0.23"
ThisBuild / tlMimaPreviousVersions ++= (0 to 11).map(y => s"0.23.$y").toSet
ThisBuild / developers := List(
  tlGitHubDev("rossabaker", "Ross A. Baker")
)

val Scala213 = "2.13.8"
ThisBuild / crossScalaVersions := Seq("2.12.17", Scala213)
ThisBuild / scalaVersion := Scala213

lazy val root = project.in(file(".")).aggregate(playJson).enablePlugins(NoPublishPlugin)

val http4sVersion = "0.23.14"
val playJsonVersion = "2.9.2"
val munitVersion = "0.7.29"
val munitCatsEffectVersion = "1.0.7"

lazy val playJson = project
  .in(file("play-json"))
  .settings(
    name := "http4s-play-json",
    description := "Provides Play json codecs for http4s",
    startYear := Some(2018),
    libraryDependencies ++= Seq(
      "org.http4s" %%% "http4s-jawn" % http4sVersion,
      "com.typesafe.play" %%% "play-json" % playJsonVersion,
      "org.scalameta" %%% "munit-scalacheck" % munitVersion % Test,
      "org.typelevel" %%% "munit-cats-effect-3" % munitCatsEffectVersion % Test,
      "org.http4s" %%% "http4s-laws" % http4sVersion % Test,
    ),
    Compile / doc / scalacOptions += "-no-link-warnings",
  )
