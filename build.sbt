name := "logviewer"

version := "0.3"

libraryDependencies ++= Seq(
    "junit" % "junit" % "4.10",
    "org.scalatest" % "scalatest_2.9.2" % "1.7.2" withSources(), 
    "org.mockito" % "mockito-all" % "1.9.0" withSources(),
	"org.scala-lang" % "scala-swing" % "2.9.2" withSources()
    )

scalacOptions += "-deprecation"

scalaVersion := "2.9.2"

