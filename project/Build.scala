package utgenome

import sbt._
import Keys._

object ProjectBuild extends Build {

   lazy val root = Project(
     id ="sample-project",
     base = file("."),
     settings = 
       Defaults.defaultSettings 
       ++ Seq(PackageTask.packageDistTask) 
       ++ PackageTask.distSettings 
       ++ Seq(
       	  scalaVersion := "2.9.2",
	  organization := "org.utgenome.sample",
       	  version := "1.0-SNAPSHOT",
       	  scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked"),
    	  parallelExecution := true,
    	  crossPaths := false,
       	  libraryDependencies ++= Seq(
             "org.scalatest" %% "scalatest" % "2.0.M1" % "test"
	  )
     )
   )
}
