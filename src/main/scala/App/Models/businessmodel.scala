package com.montecarlo

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import breeze.stats.distributions.{LogNormal, Gaussian, Uniform, Pareto}
import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._
import scalax.collection.GraphTraversal.Successors
import scala.math.exp
import java.time.LocalDate
import java.time.temporal.ChronoUnit

trait BusinessModel extends PDFunctions {this: Models =>
   
  class BusinessModel {

      //def daysBetween(d1: Date, d2: Date) = ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 *24)).toInt
      
      /**
       * contains the Monte Carlo simulator
       */
      def mcSimulator(runs: Int, data: Graph[Operation, DiEdge]): Graph[Operation, DiEdge]={

        println("******* Monte Carlo Simulator running *******") 

        /**
         * this function applies pdf function on a random number between 0-1
         */
        def mcPDFs(pdfunction: String, arg1: Double, arg2: Double): Double={

          val randomval = Uniform(0,1) //generate random number between 0-1 
          var distval: Double = 0.0 
 
          pdfunction match {
            case "normal" | "gaussian" => {
               do {
                  distval = Gaussian(arg1, arg2).inverseCdf(randomval.sample)
               } while(distval < 0)
             }
             case "log_normal" => {
               do {
                 distval = LogNormal(arg1, arg2).inverseCdf(randomval.sample)
               } while(distval < 0)
             }
             case "inv_log_normal" => {
               do {
                 distval = 2 - (LogNormal(arg1, arg2).inverseCdf(randomval.sample))
               } while(distval > 2 | distval < 0)
             }
             case "pareto" => {
               do {
                 distval = BPPareto(arg1, arg2).inverseCdf(randomval.sample) 
               } while(distval < 0)
             }
             case _ => println(f"pdf function: [${pdfunction}] unknown")
          }
          distval 
        }
        /**
         * This function calculates the duration of a task/operation using its pdf function
         */
        def mcDurationCalc(node:Operation): Int = (mcPDFs(node.pdffuncdur, node.pdfdurargs(0), node.pdfdurargs(1)) * node.bcdurbpp).toInt

        /**
         * This function calculates the cost of a task/operation using its pdf function
         */
        def mcCostCalc(node: Operation, duration: Option[Double]): Double = {

         var cost: Double = 0.0
         val distval = mcPDFs(node.pdffunccost, node.pdfcostargs(0), node.pdfcostargs(1))
         if (node.bcdayratebpp.isDefined) {
           cost =  distval * node.bcdayratebpp.getOrElse(0.0) * duration.getOrElse(0.0)
         }
         else {
           cost = distval * node.bconeoffcostbpp.getOrElse(0.0)
         }
         cost
        }

        @tailrec
        def mc(its: Int) {

          if (its > 0) {

            var runcost: Double = 0.0

            data.topologicalSort.fold(
              cycleNode => println("Error CycleNode"),
              _ foreach { node => if(node.toOuter!=root){

                val duration = mcDurationCalc(node.toOuter)
                val cost = mcCostCalc(node.toOuter, Some(duration))
                var temp: LocalDate = null

                if(node.diPredecessors.find(_.toOuter==root) == None){ 
                  val longestpred = node.diPredecessors.maxBy(_.toOuter.mcres.last.endate)(_ compareTo _).toOuter

                  node.toOuter.predefstartdate match {
                    case None => {
                      temp = LocalDate.from(longestpred.mcres.last.endate).plusDays(1)
                    }
                    case Some(predefstartdate) => {
                      if(predefstartdate.isBefore(longestpred.mcres.last.endate))
                        temp = LocalDate.from(longestpred.mcres.last.endate).plusDays(1)
                      else 
                        temp = LocalDate.from(predefstartdate)
                    }
                  }
                }
                else { 
                  temp = LocalDate.from(node.toOuter.predefstartdate.get)
                }

                val startdate = LocalDate.from(temp) 
                val endate = LocalDate.from(startdate).plusDays(duration)

                //println(f"node name: ${node.name}, startdate: ${startdate}, endate: ${endate}")
                runcost += cost
                node.mcres += new MCResult(startdate, endate, duration, cost)
              }
            })

            totalcost += runcost
            val startdatescen = data.nodes.filter(_.toOuter!=root).minBy(_.toOuter.mcres.last.startdate)(_ compareTo _).toOuter.mcres.last.startdate
            val endatescen = data.nodes.filter(_.toOuter!=root).maxBy(_.toOuter.mcres.last.endate)(_ compareTo _).toOuter.mcres.last.endate
            totaldur += ChronoUnit.DAYS.between(startdatescen, endatescen) 
            mc(its-1)
          }
        }
        mc(runs)
        println("******* End of the simulation *******") 
        data
    }
  }
}
