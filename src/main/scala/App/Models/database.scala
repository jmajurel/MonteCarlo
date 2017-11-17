package com.montecarlo.model

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.{Map => MMap}
import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._
import java.time.LocalDateTime

/** 
 *  Operation class represents the task operations contain in the input file.
 */
case class Operation (
  val name:String, 
  val predecessor: List[String],
  val predefstartdate: Option[LocalDateTime],      
  val bcdurext: Int,
  val bcdurbpp: Int,
  val bconeoffcostext: Option[Double],
  val bcdayratext: Option[Double],    
  val bconeoffcostbpp: Option[Double],
  val bcdayratebpp: Option[Double],   
  val pdffuncdur: String,
  val pdfdurargs: Vector[Double],      
  val pdffunccost: String,
  val pdfcostargs: Vector[Double]      
){
  val mcres = ListBuffer[MCResult]()
}

class MCResult (
  val startdate: LocalDateTime,
  val endate: LocalDateTime,
  val duration: Double,
  val cost: Double
)

trait Database extends FileManager {//this: Models =>


  /*class MCResult {
    val startdate: GregorianCalendar = new GregorianCalendar()
    val endate: GregorianCalendar = new GregorianCalendar()
    var duration: Double = 0.0
    var cost: Double = 0.0
    override def toString = { s"startdate: [${startdate}], endate: [${endate}], duration: [${duration}], cost: [${cost}]" }
  }*/ 

  /**
  * create root task instance
  */
  val root = Operation (
    name = "root",
    predecessor = List[String](),
    predefstartdate = None,
    bcdurext = 0,
    bcdurbpp = 0,
    bconeoffcostext = None,
    bcdayratext = None,
    bconeoffcostbpp = None,
    bcdayratebpp = None,
    pdffuncdur = "",
    pdfdurargs = Vector[Double](),
    pdffunccost = "",
    pdfcostargs = Vector[Double]()
  )

  class Database {

    val filemanager = new FileManager()
    private var graphdata = Graph[Operation, DiEdge]()
    private var totalcost = List[Double]()
    private var totaldur = List[Double]()

    /**
     * load the graph from the input file into the database
     */
    def loadIO(scenario: String){

      graphdata = Graph[Operation, DiEdge]()
      /*root.mcrescost = new Results
      root.mcresdur = new Results*/
      //root.mcres = ListBuffer[MCResults]()
      //
      val datamapping = MMap("root"-> root) ++ filemanager.read(scenario) 
      for((name, node) <- datamapping) if(name != "root") {
        if (node.predecessor.isEmpty){
          graphdata += (datamapping("root") ~> node)
        }
        else {
          for (pred <- node.predecessor)
            graphdata += (datamapping(pred) ~> node)
        }
      }
    }

    /**
     * extract data from database and generate output file
     */
    def extractIO(scenario: String) = filemanager.write(scenario, totalcost, totaldur)
    def readDBGraph: Graph[Operation, DiEdge] = graphdata 
    def updateDBGraph(newdata: Graph[Operation, DiEdge]) = graphdata = newdata
    def readDBCosts:List[Double] = totalcost 
    def updateDBCosts(newcosts: List[Double]) = totalcost = newcosts
    def readDBDurations:List[Double] = totaldur 
    def updateDBDurations(newdurs: List[Double]) = totaldur = newdurs

    /**
     * display the current database
     */
    def displayDB = println("Database: "+(graphdata.nodes mkString "\n"))
  }
}
