import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.montecarlo",
      scalaVersion := "2.12.1",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Monte Carlo",
    libraryDependencies ++= projectDeps,
    unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/ext/jfxrt.jar")),
    fork in run := true
  )
  
enablePlugins(JavaAppPackaging)
enablePlugins(WindowsPlugin)

/*// general package information (can be scoped to Windows)
maintainer := "Josh Suereth <joshua.suereth@typesafe.com>"
packageSummary := "test-windows"
packageDescription := """Test Windows MSI."""

// wix build information
wixProductId := "ce07be71-510d-414a-92d4-dff47631848a"
wixProductUpgradeId := "4552fb0e-e257-4dbd-9ecb-dba9dbacf424"
*/

