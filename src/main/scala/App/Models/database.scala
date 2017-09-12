package com.montecarlo

import scala.collection.mutable.ListBuffer
import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._


trait Database extends FileManager {this: Models =>

  class Results {
    var min: Double = 0
    var mean: Double = 0
    var max: Double = 0
    var rowresults = ListBuffer[Double]()
    def display{
        println("max: "+max)
        println("min: "+min)
        println("mean: "+mean)
    }
  }

  /** 
   *  Operation class represents the task operations contain in the input file.
   */
  case class Operation (
    val name:String, 
    val predecessor: List[String],
    val startdate: Option[String],      
    val bcdurext:Double,
    val bcdurbpp: Double,
    val bconeoffcostext: Option[Double],
    val bcdayratext: Option[Double],    
    val bconeoffcostbpp: Option[Double],
    val bcdayratebpp: Option[Double],   
    val pdffuncdur: String,
    val pdfdurargs: Vector[Double],      
    val pdffunccost: String,
    val pdfcostargs: Vector[Double]      
  ){
    var mcresdur = new Results 
    var mcrescost = new Results 
  }
  /**
  * create root task instance
  */
  val root = Operation (
    name = "root",
    predecessor = List[String](),
    startdate=None,
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

    /**
     * load the graph from the input file into the database
     */
    def loadIO(scenario: String){
      graphdata = Graph[Operation, DiEdge]()
      root.mcrescost = new Results
      root.mcresdur = new Results
      val datamapping = filemanager.read(scenario)
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
    def extractIO(scenario: String) = filemanager.write(scenario, graphdata)
    def readDB: Graph[Operation, DiEdge] = graphdata 
    def writeDB(newdata: Graph[Operation, DiEdge]) = graphdata = newdata

    /**
     * display the current database
     *///println("Database: "+(graphdata.nodes mkString "\n" ))
    def displayDB = println("Database: "+(graphdata.nodes mkString "\n"))
  }
}
