name := "text-utils"
organization := "software.purpledragon"

scalaVersion := "2.13.2"
crossScalaVersions := Seq("2.11.12", "2.12.11", scalaVersion.value)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.1.2" % Test
)

organizationName := "Michael Stringer"
startYear := Some(2020)
licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt"))

developers := List(
  Developer("stringbean", "Michael Stringer", "@the_stringbean", url("https://github.com/stringbean"))
)

organizationHomepage := Some(url("https://purpledragon.software"))
homepage := Some(url("https://github.com/stringbean/text-utils"))
scmInfo := Some(
  ScmInfo(url("https://github.com/stringbean/text-utils"), "https://github.com/stringbean/text-utils.git"))

bintrayPackageLabels := Seq("scala", "text")

import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

releasePublishArtifactsAction := PgpKeys.publishSigned.value
releaseCrossBuild := true

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  pushChanges
)
