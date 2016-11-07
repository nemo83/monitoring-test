name := """monitoring-test"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.typesafe.akka" %% "akka-stream" % "2.4.11",
  "org.lyranthe.prometheus" %% "client" % "0.5.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

