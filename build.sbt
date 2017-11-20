import Dependencies._

lazy val root = (project in file("."))
  .settings(
    inThisBuild(
      List(
        organization := "com.montecarlo",
        scalaVersion := "2.12.2",
        version      := "v04.00"
      )
    ),
    name := "Monte Carlo",
    libraryDependencies ++= projectDeps,
    unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/ext/jfxrt.jar")),
    assemblyJarName in assembly := ("BPPMonteCarlo_" + version.value + ".jar"),
    fullClasspath in assembly += file(System.getenv("JAVA_HOME") + "/jre/lib/ext/jfxrt.jar"),
    mainClass in assembly := Some("com.montecarlo.Main"),

    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x => MergeStrategy.first
    },
    test in assembly := {},
    fork := true
  )
resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
