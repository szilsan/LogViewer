name := "logviewer"

version := "0.3"

libraryDependencies ++= Seq(
    "junit" % "junit" % "4.10",
    "org.scalatest" % "scalatest_2.9.0" % "1.7.2",
    "org.mockito" % "mockito-all" % "1.9.0"
    )

scalacOptions += "-deprecation"

scalaVersion := "2.9.0"

