ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "play-json-ast",
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-json" % "2.9.2"
    )
  )
