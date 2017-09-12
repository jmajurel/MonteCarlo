package com.montecarlo

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import breeze.stats.distributions.{LogNormal, Gaussian, Uniform, Pareto}
import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._
import scalax.collection.GraphTraversal.Successors
import scala.math.exp

trait BusinessModel extends PDFunctions {this: Models =>
   
  class BusinessModel {

      /**
       * perform statistic analysis on generated data
       */
      def statistic(scenario: String, data: Graph[Operation, DiEdge]): Graph[Operation, DiEdge] = { 

        val costscale = 1000000
        //println("root: "+((data get root).toOuter.mcrescost.rowresults))
        val traverser = (data get root).outerNodeTraverser.withDirection(Successors)

        traverser.foreach ((node: Operation) => if(node!=root) {
          //println("name :"+node.name)
          if (node.mcresdur.rowresults.nonEmpty) {
            //println("node rowresults dur:" +node.mcresdur.rowresults)
            root.mcresdur.max += node.mcresdur.rowresults.max.toInt  
            root.mcresdur.min += node.mcresdur.rowresults.min.toInt
            root.mcresdur.mean += ((node.mcresdur.rowresults.sum) / node.mcresdur.rowresults.size).toInt
          }

          if (node.mcrescost.rowresults.nonEmpty) {
            //println("node rowresults cost:" +node.mcrescost.rowresults)
            //println("pdf cost args:"+node.pdfcostargs)
            root.mcrescost.max += node.mcrescost.rowresults.max / costscale 
            root.mcrescost.min += node.mcrescost.rowresults.min / costscale  
            root.mcrescost.mean += ((node.mcrescost.rowresults.sum) / node.mcrescost.rowresults.size) / costscale 
          }
        })
        data
      }

      /**
       * contains the Monte Carlo simulator
       */
      def mcSimulator(runs: Int, data: Graph[Operation, DiEdge]): Graph[Operation, DiEdge]={

        println("******* Monte Carlo Simulator running *******") 

        @tailrec
        def mc(its: Int) {

          var totalcost: Double = 0
          var totaldur: Double = 0
          val costscale = 1000000
          if (its > 0) {
            val traverser = (data get root).outerNodeTraverser.withDirection(Successors)
            traverser.foreach((node: Operation) => if(node!=root){
              //println("node :" +node)

              var randomvaluedur = Uniform(0,1).sample //generate random number between 0-1 for the duration calculation.
              var pdfdur: Double = 0.0 

              node.pdffuncdur match {

                case "normal" | "gaussian" => {
                  do{
                     randomvaluedur = Uniform(0,1).sample
                     pdfdur = Gaussian(node.pdfdurargs(0), node.pdfdurargs(1)).inverseCdf(randomvaluedur)
                  }while(pdfdur < 0)

                  node.mcresdur.rowresults += pdfdur * node.bcdurbpp
                }
                case "log_normal" => {
                  do{
                    randomvaluedur = Uniform(0,1).sample
                    pdfdur = LogNormal(node.pdfdurargs(0), node.pdfdurargs(1)).inverseCdf(randomvaluedur)
                  }while(pdfdur < 0)

                  node.mcresdur.rowresults += pdfdur * node.bcdurbpp
                }
                case "inv_log_normal" => {
                  do{
                    randomvaluedur = Uniform(0,1).sample
                    //println("invlogdur: " +((LogNormal(node.pdfdurargs(0), node.pdfdurargs(1))).inverseCdf(randomvaluedur)))
                    pdfdur = 2 - (LogNormal(node.pdfdurargs(0), node.pdfdurargs(1)).inverseCdf(randomvaluedur))
                  }while(pdfdur > 2 | pdfdur < 0)
                  node.mcresdur.rowresults += pdfdur * node.bcdurbpp
                }
                case "pareto" => {
                  do{
                    randomvaluedur = Uniform(0,1).sample
                    pdfdur = BPPareto(node.pdfdurargs(0), node.pdfdurargs(1)).inverseCdf(randomvaluedur) 
                  }while(pdfdur < 0)

                  node.mcresdur.rowresults += pdfdur * node.bcdurbpp
                }
                case _ => println("pdf duration function unknown")
              }

              var randomvaluecost = Uniform(0,1).sample //generate random number between 0-1 for the cost calculation
              var rescost: Double = 0.0
              var pdfcost: Double = 0.0
                node.pdffunccost match {

                  case "normal" | "gaussian" => {
                    do {
                      randomvaluecost = Uniform(0,1).sample 
                      pdfcost = Gaussian(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost)
                    } while(pdfcost < 0)

                    if (node.bcdayratebpp.isDefined)
                      rescost = pdfcost * node.bcdayratebpp.getOrElse(0.0) * node.mcresdur.rowresults.last
                    else
                      rescost = pdfcost * node.bconeoffcostbpp.getOrElse(0.0)

                    node.mcrescost.rowresults += rescost 
                  }
                  case "log_normal" => {
                    do {
                      randomvaluecost = Uniform(0,1).sample 
                      pdfcost = LogNormal(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost)
                    } while(pdfcost < 0)

                    if (node.bcdayratebpp.isDefined)
                      rescost = pdfcost * node.bcdayratebpp.getOrElse(0.0) * node.mcresdur.rowresults.last
                    else
                      rescost = pdfcost * node.bconeoffcostbpp.getOrElse(0.0) 

                    node.mcrescost.rowresults += rescost 
                  }
                  case "inv_log_normal" => {
                    do {
                      randomvaluecost = Uniform(0,1).sample 
                      pdfcost  = 2 - LogNormal(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost)
                    } while(pdfcost > 2 | pdfcost < 0)

                    if (node.bcdayratebpp.isDefined)
                      rescost = pdfcost * node.bcdayratebpp.getOrElse(0.0) * node.mcresdur.rowresults.last
                    else
                      rescost = pdfcost * node.bconeoffcostbpp.getOrElse(0.0)
                    
                    node.mcrescost.rowresults += rescost 
                  }
                  case "pareto" => {
                    do {
                      randomvaluecost = Uniform(0,1).sample 
                      pdfcost = BPPareto(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost)
                    } while(pdfcost < 0)
                    
                    if (node.bcdayratebpp.isDefined)
                      rescost = pdfcost * node.bcdayratebpp.getOrElse(0.0) * node.mcresdur.rowresults.last
                    else
                      rescost = pdfcost *node.bconeoffcostbpp.getOrElse(0.0)

                    node.mcrescost.rowresults += rescost 
                  }

                  case _ => println("pdf cost function unknown")
               } 
               totaldur += node.mcresdur.rowresults.last
               totalcost += node.mcrescost.rowresults.last
            })
            root.mcrescost.rowresults += totalcost / costscale 
            root.mcresdur.rowresults += totaldur
            mc(its-1)
          }
        }
      mc(runs)
      println("******* End of the simulation *******") 
      data
    }
  }
}
