name := "text-utils"
organization := "software.purpledragon"

scalaVersion := "2.13.6"
crossScalaVersions := Seq("2.11.12", "2.12.14", scalaVersion.value)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.9" % Test
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
  ScmInfo(url("https://github.com/stringbean/text-utils"), "scm:git:git@github.com:stringbean/text-utils.git"))
publishTo := sonatypePublishToBundle.value
versionScheme := Some("semver-spec")

import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

releasePublishArtifactsAction := PgpKeys.publishSigned.value
releaseCrossBuild := true

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  releaseStepTask(Compile / headerCheck),
  releaseStepTask(Test / headerCheck),
  releaseStepTask(Compile / scalafmtCheck),
  releaseStepTask(Test / scalafmtCheck),
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  releaseStepTask(sonatypeBundleRelease),
  releaseStepTask(ghpagesPushSite),
  setNextVersion,
  commitNextVersion,
  pushChanges
)

mimaPreviousArtifacts := Set("software.purpledragon" %% "text-utils" % "1.3.0")

// documentation settings
enablePlugins(SiteScaladocPlugin, GhpagesPlugin, ParadoxSitePlugin)

autoAPIMappings := true

git.remoteRepo := scmInfo.value.get.connection.replace("scm:git:", "")
ghpagesNoJekyll := true

SiteScaladoc / siteSubdirName := "api"
addMappingsToSiteDir(SiteScaladoc / packageDoc /  mappings, SiteScaladoc / siteSubdirName)
Compile / paradoxProperties ++= Map(
  "scaladoc.base_url" -> ".../api"
)
