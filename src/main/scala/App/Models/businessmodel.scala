package com.montecarlo.model

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import breeze.stats.distributions.{LogNormal, Gaussian, Uniform, Pareto}
import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._
import scalax.collection.GraphTraversal.Successors
import scala.math.exp
import scala.math.abs
import scala.math.ceil
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

trait BusinessModel extends PDFunctions {//this: Models =>
   
  class BusinessModel {

      /**
       * Convert decimal number of days to number of seconds
       */
      def convertDaysToSec(days: Double): Double = days*24*60*60

      /**
       * this function applies pdf function on a random number between 0-1
       */
      def mcPDFs(pdfunction: String, arg1: Double, arg2: Double): Double={

        val randomval = Uniform(0,1) //generate random number between 0-1 
        //val randomval = 0.5
        var distval: Double = 0.0 

        pdfunction match {
          case "normal" | "gaussian" => {
             do {
                distval = Gaussian(arg1, arg2).inverseCdf(randomval.sample)
                //distval = Gaussian(arg1, arg2).inverseCdf(randomval)
             } while(distval < 0)
           }
           case "log_normal" => {
             do {
               distval = LogNormal(arg1, arg2).inverseCdf(randomval.sample)
               //distval = LogNormal(arg1, arg2).inverseCdf(randomval)
             } while(distval < 0)
           }
           case "inv_log_normal" => {
             do {
               distval = 2 - (LogNormal(arg1, arg2).inverseCdf(randomval.sample))
               //distval = 2 - (LogNormal(arg1, arg2).inverseCdf(randomval))
             } while(distval > 2 | distval < 0)
           }
           case "pareto" => {
             do {
               distval = BPPareto(arg1, arg2).inverseCdf(randomval.sample) 
               //distval = BPPareto(arg1, arg2).inverseCdf(randomval) 
             } while(distval < 0)
           }
           case "half_normal" => {
              do {
                distval = Gaussian(arg1, arg2).inverseCdf(randomval.sample)
                //distval = Gaussian(arg1, arg2).inverseCdf(randomval)
              } while(distval < 0 | distval < arg1)
           }
           case "inv_half_normal" => {
              do {
                distval = Gaussian(arg1, arg2).inverseCdf(randomval.sample)
                //distval = Gaussian(arg1, arg2).inverseCdf(randomval)
              } while(distval < 0 | distval > arg1)
           }
           case "extreme" => {
              do {
                distval = BPPExtreme(arg1, arg2).inverseCdf(randomval.sample)
                //distval = BPPExtreme(arg1, arg2).inverseCdf(randomval)
             } while(distval < 0)
           }
           case _ => println(f"pdf function: [${pdfunction}] unknown")
        }
        distval 
      }

      /**
       * This function calculates the duration of a task/operation using its pdf function
       */
      def mcDurationCalc(node: Operation): Double = (mcPDFs(node.pdffuncdur, node.pdfdurargs(0), node.pdfdurargs(1)) * node.bcdurbpp)
      
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
  
      /**
       * contains the Monte Carlo simulator
       */
      def mcSimulator(runs: Int, data: Graph[Operation, DiEdge]): (Graph[Operation, DiEdge], List[Double], List[Double])={

        println("******* Monte Carlo Simulator running *******") 
        val totalcost = ListBuffer[Double]()
        val totaldur = ListBuffer[Double]()



        @tailrec
        def mc(its: Int) {

          if (its > 0) {

            var runcost: Double = 0.0

            data.topologicalSort.fold(
              cycleNode => println("Error CycleNode"),
              _ foreach { node => if(node.toOuter.name!="root"){

                val duration = mcDurationCalc(node.toOuter)
                val cost = mcCostCalc(node.toOuter, Some(duration))
                var temp: LocalDateTime = null

                if(node.diPredecessors.find(_.toOuter.name=="root") == None){ 
                  val longestpred = node.diPredecessors.maxBy(_.toOuter.mcres.last.endate)(_ compareTo _).toOuter

                  node.toOuter.predefstartdate match {
                    case None => {
                      temp = LocalDateTime.from(longestpred.mcres.last.endate).plusSeconds(1)
                    }
                    case Some(predefstartdate) => {
                      if(predefstartdate.isBefore(longestpred.mcres.last.endate))
                        temp = LocalDateTime.from(longestpred.mcres.last.endate).plusSeconds(1)
                      else 
                        temp = LocalDateTime.from(predefstartdate)
                    }
                  }
                }
                else { 
                  temp = LocalDateTime.from(node.toOuter.predefstartdate.get)
                }

                val startdate = LocalDateTime.from(temp) 
                val endate = LocalDateTime.from(startdate).plusSeconds(convertDaysToSec(duration).toLong)

                //println(f"node name: ${node.name}, startdate: ${startdate}, endate: ${endate}")
                runcost += cost
                node.mcres += new MCResult(startdate, endate, duration, cost)
              }
            })

            totalcost += runcost
            val startdatescen = data.nodes.filter(_.toOuter.name!="root").minBy(_.toOuter.mcres.last.startdate)(_ compareTo _).toOuter.mcres.last.startdate
            val endatescen = data.nodes.filter(_.toOuter.name!="root").maxBy(_.toOuter.mcres.last.endate)(_ compareTo _).toOuter.mcres.last.endate
            totaldur += ChronoUnit.DAYS.between(startdatescen, endatescen) 
            mc(its-1)
          }
        }
        mc(runs)
        println("******* End of the simulation *******") 
        (data, totalcost.toList, totaldur.toList)
    }
  }
}
