package com.montecarlo

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.math.exp
import scala.math.pow
import breeze.stats.distributions.{LogNormal, Gaussian, Uniform, Pareto}
import breeze.linalg.randomDouble
import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._
import scalax.collection.GraphTraversal.Successors

trait BusinessModel {this: Models =>

  case class UtimatePareto(mu: Double, sigma: Double) {
    def inverseCdf(y: Double) = mu/pow((1-y), (1/sigma)) 
  }
    
  object BusinessModel {

      def statistic(scenario: String) {

        val sumres = MCSumResult(scenario)
        val traverser = (graphdata get root).outerNodeTraverser.withDirection(Successors)

        traverser.foreach ((node: Operation) => if(node!=root){
          if (node.mcresdur.nonEmpty){
            sumres.maxdur += node.mcresdur.max  
            println(f"node: ${node.name}, maxdur: ${node.mcresdur.max.toInt}")
            sumres.mindur += node.mcresdur.min
            println(f"node: ${node.name}, mindur: ${node.mcresdur.min.toInt}")
            sumres.meandur += ((node.mcresdur.max - node.mcresdur.min) / node.mcresdur.size)
          }
          if (node.mcrescost.nonEmpty){
            sumres.maxcost += node.mcrescost.max  
            println(f"node: ${node.name}, maxcost: ${node.mcrescost.max}")
            sumres.mincost += node.mcrescost.min
            println(f"node: ${node.name}, mincost: ${node.mcrescost.min}")
            sumres.meancost += ((node.mcrescost.max - node.mcrescost.min) / node.mcrescost.size)
          }
        })
      }

      /**
       * contains the Monte Carlo simulator
       */
      def mcSimulator(runs: Int) {

        println("******* Monte Carlo Simulator running *******") 

        @tailrec
        def mc(its: Int) {

          if (its > 0) {
            val traverser = (graphdata get root).outerNodeTraverser.withDirection(Successors)
            traverser.foreach((node: Operation) => if(node!=root){

              var randomvaluedur = Uniform(0,1).sample //generate random number between 0-1 for the duration calculation.
              var resdur: Double = 0.0 

              node.pdffuncdur match {

                case "normal" | "gaussian" => {
                  do{
                     randomvaluedur = Uniform(0,1).sample
                     resdur = (Gaussian(node.pdfdurargs(0), node.pdfdurargs(1)).inverseCdf(randomvaluedur) * node.bcdurbpp)
                  }while(resdur < 0)

                  node.mcresdur +=resdur
                }
                case "log_normal" => {
                  do{
                    randomvaluedur = Uniform(0,1).sample
                    resdur = (LogNormal(node.pdfdurargs(0), node.pdfdurargs(1)).inverseCdf(randomvaluedur) * node.bcdurbpp) 
                  }while(resdur < 0)

                  node.mcresdur += resdur
                }
                case "inv_log_normal" => {
                  do{
                    randomvaluedur = Uniform(0,1).sample
                    resdur = 2 - (LogNormal(node.pdfdurargs(0), node.pdfdurargs(1)).inverseCdf(randomvaluedur) * node.bcdurbpp)
                  }while(resdur > 2 | resdur < 0)

                  node.mcresdur += resdur
                }
                case "pareto" => {
                  do{
                    randomvaluedur = Uniform(0,1).sample
                    resdur = (UtimatePareto(node.pdfdurargs(0), node.pdfdurargs(1)).inverseCdf(randomvaluedur) *  node.bcdurbpp)
                  }while(resdur < 0)

                  node.mcresdur += resdur
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

                    node.mcrescost += rescost 
                  }
                  case "log_normal" => {
                    do{
                      randomvaluecost = Uniform(0,1).sample 
                      if(node.bconeoffcostbpp.isDefined)
                        rescost = (LogNormal(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost) * node.bconeoffcostbpp.getOrElse(0.0)) 
                      else if(node.bcdayratebpp.isDefined)
                        rescost = (LogNormal(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost) * node.bcdayratebpp.getOrElse(0.0) * resdur)
                    }while(rescost < 0)

                    node.mcrescost += rescost
                  }
                  case "inv_log_normal" => {
                    randomvaluecost = Uniform(0,1).sample 
                    if(node.bconeoffcostbpp.isDefined)
                      rescost = 2 - (LogNormal(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost) * node.bconeoffcostbpp.getOrElse(0.0))
                    else if(node.bcdayratebpp.isDefined)
                      rescost = (2 - LogNormal(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost)) * node.bcdayratebpp.getOrElse(0.0) * resdur

                    node.mcrescost += rescost 
                  }
                  case "pareto" => {
                    do{
                      randomvaluecost = Uniform(0,1).sample 
                      if(node.bconeoffcostbpp.isDefined)
                        rescost = (UtimatePareto(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost) *  node.bconeoffcostbpp.getOrElse(0.0))
                      else if(node.bcdayratebpp.isDefined)
                        rescost = (UtimatePareto(node.pdfcostargs(0), node.pdfcostargs(1)).inverseCdf(randomvaluecost) *  node.bconeoffcostbpp.getOrElse(0.0) * resdur)
                    }while(rescost < 0)

                    node.mcrescost += rescost
                  }

                  case _ => println("pdf cost function unknown")
               } 
            })
            mc(its-1)
          }
        }
      mc(runs)
      println("******* End of the simulation *******") 
    }
  }
}
