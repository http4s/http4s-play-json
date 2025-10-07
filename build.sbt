ThisBuild / tlBaseVersion := "0.23"
// 0.23.0-11 were tags from this project before it was split out from the main http4s repository
ThisBuild / tlMimaPreviousVersions ++= (0 to 11).map(y => s"0.23.$y").toSet
ThisBuild / developers := List(
  tlGitHubDev("rossabaker", "Ross A. Baker"),
  tlGitHubDev("henricook", "Henri Cook"),
  tlGitHubDev("matmannion", "Mat Mannion"),
)

headerLicense := Some(HeaderLicense.ALv2("[yyyy]", "[Name Of Copyright Owner]"))

ThisBuild / githubWorkflowJavaVersions := List("21", "17", "11").map(JavaSpec.temurin)
ThisBuild / tlJdkRelease := Some(11)

// Test all Java versions with all Scala versions
ThisBuild / githubWorkflowBuildMatrixExclusions := Seq.empty

// Add coverage job
ThisBuild / githubWorkflowAddedJobs ++= Seq(
  WorkflowJob(
    id = "coverage",
    name = "Generate coverage report",
    scalas = List(Scala213),
    javas = List(JavaSpec.temurin("8")),
    steps = githubWorkflowJobSetup.value.toList ++
      List(
        WorkflowStep.Sbt(List("coverage", "test", "coverageReport")),
        WorkflowStep.Use(
          UseRef.Public("codecov", "codecov-action", "v4"),
          cond = Some("github.event_name != 'pull_request'"),
        ),
      ),
  )
)

val Scala213 = "2.13.17"
val Scala3 = "3.3.6"
ThisBuild / scalaVersion := Scala213
ThisBuild / crossScalaVersions := Seq(Scala213, Scala3)
ThisBuild / tlVersionIntroduced := Map("3" -> "0.23.12")

lazy val root = project.in(file(".")).aggregate(playJson).enablePlugins(NoPublishPlugin)

val http4sVersion = "0.23.32"
val playJsonVersion = "3.0.5"
val munitVersion = "1.2.0"
val munitCatsEffectVersion = "2.1.0"

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
