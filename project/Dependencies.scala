import sbt._

object Dependencies {

  // Libraries
  lazy val scalafx ="org.scalafx" %% "scalafx" % "8.0.102-R11"
  lazy val jcommon = "org.jfree" % "jcommon" % "1.0.23"
  lazy val jfreechart = "org.jfree" % "jfreechart" % "1.0.19"
  
  // Projects
  val projectDeps = 
    Seq(scalafx,jcommon,jfreechart)  
}
