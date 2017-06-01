import Dependencies._

lazy val commonSettings = Seq(
	organization := "BPP-TECH",
	scalaVersion := "2.12.2",
	version      := "0.1.0-SNAPSHOT",
	name := "Montecarlo_demo01"
)

lazy val root = (project in file(".")).
	settings(
		commonSettings,
		libraryDependencies ++= projectDeps,
		unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/ext/jfxrt.jar")),
		fork in run := true
	)
