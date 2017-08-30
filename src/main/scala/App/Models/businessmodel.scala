package com.montecarlo

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import breeze.stats.distributions.{LogNormal, Gaussian, Uniform, Pareto}
import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._
import scalax.collection.GraphTraversal.Successors

trait BusinessModel extends PDFunctions {this: Models =>
   
  class BusinessModel {

      /**
       * perform statistic analysis on generated data
       */
      def statistic(scenario: String, data: Graph[Operation, DiEdge]): SummaryRes={ 

        val sumres = SummaryRes(scenario)
        val root = data.nodes.head.toOuter
        val traverser = (data get root).outerNodeTraverser.withDirection(Successors)

        traverser.foreach ((node: Operation) => if(node!=root) {
          if (node.mcresdur.rowresults.nonEmpty) {
            sumres.dur.max += node.mcresdur.rowresults.max  
            sumres.dur.min += node.mcresdur.rowresults.min
            sumres.dur.mean += ((node.mcresdur.rowresults.max - node.mcresdur.rowresults.min) / node.mcresdur.rowresults.size)
            sumres.dur.rowresults = sumres.dur.rowresults.zip(node.mcresdur.rowresults).map{ case(x, y) => x+y }
          }

          if (node.mcrescost.rowresults.nonEmpty) {
            sumres.cost.max += node.mcrescost.rowresults.max  
            sumres.cost.min += node.mcrescost.rowresults.min
            sumres.cost.mean += ((node.mcrescost.rowresults.max - node.mcrescost.rowresults.min) / node.mcrescost.rowresults.size)
            sumres.cost.rowresults = sumres.cost.rowresults.zip(node.mcrescost.rowresults).map{ case(x, y) => x+y }
          }
        })
        sumres
      }

      /**
       * contains the Monte Carlo simulator
       */
      def mcSimulator(runs: Int, data: Graph[Operation, DiEdge]): Graph[Operation, DiEdge]={

        println("******* Monte Carlo Simulator running *******") 

        @tailrec
        def mc(its: Int) {

          if (its > 0) {
            val root = data.nodes.head.toOuter
            val traverser = (data get root).outerNodeTraverser.withDirection(Successors)
            traverser.foreach((node: Operation) => if(node!=root){

              var randomvaluedur = Uniform(0,1).sample //generate random number between 0-1 for the duration calculation.
              var resdur: Double = 0.0 

              node.pdffuncdur match {

                case "normal" | "gaussian" => {
                  do{
                     randomvaluedur = Uniform(0,1).sample
                     resdur = (Gaussian(node.pdfdurargs(0), node.pdfdurargs(1)).inverseCdf(randomvaluedur) * node.bcdurbpp)
                  }while(resdur < 0)

                  node.mcresdur.rowresults += resdur
                }
                case "log_normal" => {
                  do{
                    randomvaluedur = Uniform(0,1).sample
                    resdur = (LogNormal(node.pdfdurargs(0), node.pdfdurargs(1)).inverseCdf(randomvaluedur) * node.bcdurbpp) 
                  }while(resdur < 0)

                  node.mcresdur.rowresults += resdur
                }
                case "inv_log_normal" => {
                  do{
                    randomvaluedur = Uniform(0,1).sample
                    resdur = 2 - (LogNormal(node.pdfdurargs(0), node.pdfdurargs(1)).inverseCdf(randomvaluedur) * node.bcdurbpp)
                  }while(resdur > 2 | resdur < 0)

                  node.mcresdur.rowresults += resdur
                }
                case "pareto" => {
                  do{
                    randomvaluedur = Uniform(0,1).sample
                    resdur = (BPPareto(node.pdfdurargs(0), node.pdfdurargs(1)).inverseCdf(randomvaluedur) *  node.bcdurbpp)
                  }while(resdur < 0)

                  node.mcresdur.rowresults += resdur
                }
                case _ => println("pdf duration function unknown")
              }

              var randomvaluecost = Uniform(0,1).sample //generate random number between 0-2 for the cost calculation
              var rescost: Double = 0.0

                node.pdffunccost match {

                  case "normal" | "gaussian" => {
                    do{
                      randomvaluecost = Uniform(0,1).sample 
                      if(node.bconeoffcostbpp.isDefined)
                        rescost = (Gaussian(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost) * node.bconeoffcostbpp.getOrElse(0.0))
                      else if(node.bcdayratebpp.isDefined)
                        rescost = (Gaussian(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost) * node.bcdayratebpp.getOrElse(0.0) * resdur)
                    }while(rescost < 0)

                    node.mcrescost.rowresults += rescost 
                  }
                  case "log_normal" => {
                    do{
                      randomvaluecost = Uniform(0,1).sample 
                      if(node.bconeoffcostbpp.isDefined)
                        rescost = (LogNormal(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost) * node.bconeoffcostbpp.getOrElse(0.0)) 
                      else if(node.bcdayratebpp.isDefined)
                        rescost = (LogNormal(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost) * node.bcdayratebpp.getOrElse(0.0) * resdur)
                    }while(rescost < 0)

                    node.mcrescost.rowresults += rescost 
                  }
                  case "inv_log_normal" => {
                    randomvaluecost = Uniform(0,1).sample 
                    if(node.bconeoffcostbpp.isDefined)
                      rescost = 2 - (LogNormal(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost) * node.bconeoffcostbpp.getOrElse(0.0))
                    else if(node.bcdayratebpp.isDefined)
                      rescost = (2 - LogNormal(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost)) * node.bcdayratebpp.getOrElse(0.0) * resdur

                    node.mcrescost.rowresults += rescost 
                  }
                  case "pareto" => {
                    do{
                      randomvaluecost = Uniform(0,1).sample 
                      if(node.bconeoffcostbpp.isDefined)
                        rescost = (BPPareto(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost) *  node.bconeoffcostbpp.getOrElse(0.0))
                      else if(node.bcdayratebpp.isDefined)
                        rescost = (BPPareto(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost) *  node.bconeoffcostbpp.getOrElse(0.0) * resdur)
                    }while(rescost < 0)

                    node.mcrescost.rowresults += rescost 
                  }

                  case _ => println("pdf cost function unknown")
               } 
            })
            mc(its-1)
          }
        }
      mc(runs)
      println("******* End of the simulation *******") 
      data
    }
  }
}
