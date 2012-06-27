package utgenome.sample

import sbt._
import Keys._

object ProjectBuild extends Build {

   lazy val root = Project(
     id ="sample-project",
     base = file("."),
     settings = Defaults.defaultSettings ++ Seq(
       scalaVersion := "2.9.2",
       organization := "org.utgenome.sample",
       version := "1.0-SNAPSHOT",
       libraryDependencies ++= Seq(
          "org.scalatest" %% "scalatest" % "2.0.M1" % "test"
       )
     )
   )

}