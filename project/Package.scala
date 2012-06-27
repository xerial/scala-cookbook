package utgenome

import sbt._
import Keys._
import sbt.classpath.ClasspathUtilities

object PackageTask {
       
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
