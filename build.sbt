name := "text-utils"
organization := "software.purpledragon"

scalaVersion := "2.13.1"
crossScalaVersions := Seq("2.11.12", "2.12.10", scalaVersion.value)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.1.0" % Test
)

organizationName := "Michael Stringer"
startYear := Some(2020)
licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt"))

developers := List(
  Developer("stringbean", "Michael Stringer", "@the_stringbean", url("https://github.com/stringbean"))
)