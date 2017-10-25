import sbt._

object Dependencies {

  // Libraries
  lazy val scalafx = "org.scalafx" %% "scalafx" % "8.0.102-R11"

  lazy val jcommon = "org.jfree" % "jcommon" % "1.0.23"
  lazy val jfreechart = "org.jfree" % "jfreechart" % "1.0.19"

  lazy val apachepoi = "org.apache.poi" % "poi" % "3.16"
  lazy val apacheooxml = "org.apache.poi" % "poi-ooxml" % "3.16"
  lazy val apacheooxmlschema = "org.apache.poi" % "poi-ooxml-schemas" % "3.16"

  lazy val breeze = "org.scalanlp" %% "breeze" % "0.13.2"
  lazy val breezenatives = "org.scalanlp" %% "breeze-natives" % "0.13.2"
  lazy val breezeviz = "org.scalanlp" %% "breeze-viz" % "0.13.2"

  lazy val graphcore = "org.scala-graph" %% "graph-core" % "1.11.5"
  
  lazy val scalatest = "org.scalatest" % "scalatest_2.12" % "3.0.4" % "test"
  lazy val junit = "org.junit" % "jupiter" % "5.0.1" % "test"
  lazy val testfxcore = "org.testfx" % "testfx-core" % "4.0.1-alpha" % "test"
  lazy val testfxjunit = "org.testfx" % "testfx-junit" % "4.0.1-alpha" % "test" 

  // Projects
  val projectDeps = Seq(
    scalafx,
    jcommon,
    jfreechart,
    apachepoi,
    apacheooxml,
    apacheooxmlschema, 
    breeze,
    breezenatives,
    breezeviz,
    graphcore,
    scalatest,
    junit,
    testfxcore, 
    testfxjunit
  )
}
