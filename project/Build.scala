import sbt._
import sbt.Keys._
import sbt.classpath.ClasspathUtilities


object ProjectBuild extends Build {

  lazy val buildSettings = Defaults.defaultSettings ++ Seq[Setting[_]](
    description := "Scala Cook Book Sample Project",
    scalaVersion := "2.9.2",
    publishMavenStyle := true,
    publishArtifact in Test := false,
    otherResolvers := Seq(Resolver.file("localM2", file(Path.userHome.absolutePath + "/.m2/repository"))),
    publishLocalConfiguration <<= (packagedArtifacts, deliverLocal, checksums, ivyLoggingLevel) map {
      (arts, _, cs, level) => new PublishConfiguration(None, "localM2", arts, cs, level)
    },
    pomIncludeRepository := {
      _ => false
    },
    parallelExecution := true,
    crossPaths := false,
    resolvers ++= Seq("Typesafe repository" at "http://repo.typesafe.com/typesafe/releases",
      "UTGB Maven repository" at "http://maven.utgenome.org/repository/artifact/",
      "Xerial Maven repository" at "http://www.xerial.org/maven/repository/artifact",
      "Local Maven repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"
    ),
    scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked")
  )

  val distAllClasspaths = TaskKey[Seq[Classpath]]("dist-all-classpaths")
  val distDependencies = TaskKey[Seq[File]]("dist-dependencies")
  val distLibJars = TaskKey[Seq[File]]("dist-lib-jars")

  lazy val distSettings: Seq[Setting[_]] = Seq(
    distAllClasspaths <<= (thisProjectRef, buildStructure) flatMap allProjects(dependencyClasspath.task in Compile),
    distDependencies <<= distAllClasspaths map {
      _.flatten.map(_.data).filter(ClasspathUtilities.isArchive).distinct
    },
    distLibJars <<= (thisProjectRef, buildStructure) flatMap allProjects(packageBin.task in Compile)
  )

  def allProjects[T](task: SettingKey[Task[T]])(currentProject: ProjectRef, structure: Load.BuildStructure): Task[Seq[T]] = {
    val projects: Seq[String] = currentProject.project +: childProjectNames(currentProject, structure)
    projects flatMap {
      task in LocalProject(_) get structure.data
    } join
  }

  def childProjectNames(currentProject: ProjectRef, structure: Load.BuildStructure): Seq[String] = {
    val children = Project.getProject(currentProject, structure).toSeq.flatMap(_.aggregate)
    children flatMap {
      ref =>
        ref.project +: childProjectNames(ref, structure)
    }
  }


  object Dependencies {

    val testLib = Seq(
      "junit" % "junit" % "4.10" % "test",
      "org.scalatest" %% "scalatest" % "1.8" % "test"
    )

    val bootLib = Seq(
      "org.codehaus.plexus" % "plexus-classworlds" % "2.4"
    )

    val coreLib = Seq(
      "org.xerial.silk" % "silk-core" % "0.4"
    )
  }

  import Dependencies._

  lazy val root = Project(
    id = "scala-cookbook",
    base = file("."),
    settings = buildSettings ++ distSettings 
      ++ Seq(packageDistTask)
      ++ Seq(libraryDependencies ++= bootLib ++ testLib ++ coreLib)
  )

  lazy val packageDist: TaskKey[File] = TaskKey[File]("package-dist")

  def packageDistTask: Setting[Task[File]] = packageDist <<= (update, version, distLibJars, distDependencies, streams, target, dependencyClasspath in Runtime, classDirectory in Compile, baseDirectory) map {
    (up, ver, libs, depJars, out, target, dependencies, classDirectory, base) => {

      val distDir = target / "dist"

      out.log.info("output dir: " + distDir)
      IO.delete(distDir)
      distDir.mkdirs()

      out.log.info("Copy libraries")
      val libDir = distDir / "lib"
      libDir.mkdirs()
      (libs ++ depJars).foreach(l => IO.copyFile(l, libDir / l.getName))

      out.log.info("Create bin folder")
      val binDir = distDir / "bin"
      binDir.mkdirs()
      IO.copyDirectory(base / "src/script", binDir)

      out.log.info("Generating version info")
      IO.write(distDir / "VERSION", ver)
      out.log.info("done.")

      distDir
    }
  }
}
