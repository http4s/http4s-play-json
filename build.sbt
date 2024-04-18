ThisBuild / tlBaseVersion := "0.23"
// 0.23.0-11 were tags from this project before it was split out from the main http4s repository
ThisBuild / tlMimaPreviousVersions ++= (0 to 11).map(y => s"0.23.$y").toSet
ThisBuild / developers := List(
  tlGitHubDev("rossabaker", "Ross A. Baker"),
  tlGitHubDev("henricook", "Henri Cook"),
  tlGitHubDev("matmannion", "Mat Mannion"),
)

// play-json stopped supporting Java8 in 2.10.1
ThisBuild / githubWorkflowJavaVersions := List("11", "17", "21").map(JavaSpec.temurin)
ThisBuild / tlJdkRelease := Some(11)

val Scala212 = "2.12.19"
val Scala213 = "2.13.12"
val Scala3 = "3.3.3"
ThisBuild / scalaVersion := Scala213
ThisBuild / crossScalaVersions := Seq(Scala212, Scala213, Scala3)
ThisBuild / tlVersionIntroduced := Map("3" -> "0.23.12")

lazy val root = project.in(file(".")).aggregate(playJson).enablePlugins(NoPublishPlugin)

val http4sVersion = "0.23.26"
val playJsonVersion = "3.0.2"
val munitVersion = "1.0.0-M12"
val munitCatsEffectVersion = "2.0.0-M4"

lazy val playJson = project
  .in(file("play-json"))
  .settings(
    name := "http4s-play-json",
    description := "Provides Play json codecs for http4s",
    startYear := Some(2018),
    libraryDependencies ++= Seq(
      "org.http4s" %%% "http4s-jawn" % http4sVersion,
      "org.playframework" %%% "play-json" % playJsonVersion,
      "org.scalameta" %%% "munit-scalacheck" % munitVersion % Test,
      "org.typelevel" %%% "munit-cats-effect" % munitCatsEffectVersion % Test,
      "org.http4s" %%% "http4s-laws" % http4sVersion % Test,
    ),
  )
