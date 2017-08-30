package com.montecarlo

import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._
import scala.collection.mutable.{ListBuffer => MListBuffer}

trait Database extends FileManager {this: Models =>

  /**
  * host the summary of result of the montecarlo simulation
  */
  trait BaseResult {

    var max: Double
    var min: Double
    var mean: Double
    var rowresults: MListBuffer[Double]
    
    def display {
      println("max: "+max)
      println("min: "+min)
      println("mean: "+mean)
    }
  }

  case class CostRes() extends BaseResult
  case class DurRes() extends BaseResult
  case class SummaryRes(val scenario: String) {
    val cost = CostRes
    val dur = DurRes
  }

  /** 
   *  Operation class represents the task operations contain in the input file.
   */
  case class Operation (
    name:String, 
    predecessor: List[String],
    startdate: Option[String],      
    bcdurext:Double,
    bcdurbpp: Double,
    bconeoffcostext: Option[Double],
    bcdayratext: Option[Double],    
    bconeoffcostbpp: Option[Double],
    bcdayratebpp: Option[Double],   
    pdffuncdur: String,
    pdfdurargs: Vector[Double],      
    pdffunccost: String,
    pdfcostargs: Vector[Double]      
  ){
    val mcresdur = DurRes 
    val mcrescost = CostRes 
  }

  class Database {
    
    val filemanager = new FileManager()
    private var graphdata = Graph[Operation, DiEdge]()
    private var results = SummaryRes("scenario01")

    /**
     * load the graph from the input file into the database
     */
    def loadIO(scenario: String) {
      
      val datamapping = filemanager.read(scenario)
      for((name, node) <- datamapping) {
        if (node.predecessor.isEmpty)
          graphdata += (datamapping("root") ~> node)
        else for (pred <- node.predecessor)
          graphdata += (datamapping(pred) ~> node)
      }
    }

    /**
     * extract data from database and generate output file
     */
    def extractIO(scenario: String) = filemanager.write(scenario, results)
    def readDB: Graph[Operation, DiEdge] = graphdata 
    def writeDB(newdata: Graph[Operation, DiEdge]) = graphdata = newdata
    def writeResultDB(res: SummaryRes) = results = res

    /**
     * display the current database
     */
    def displayDB = println("Database: "+(graphdata.nodes mkString "\n" ))
  }
}
