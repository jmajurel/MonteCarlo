package com.montecarlo

import breeze.stats.distributions.{LogNormal, Gaussian, Uniform}
import breeze.linalg.randomDouble
import scala.annotation.tailrec
import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._

trait MonteCarlo {this: Models =>

  object MonteCarlo {

    def simulator(runs: Int, data: Graph[Operation,DiEdge]) {
      
      @tailrec
      def mc(its: Int, acc:List[Double]):List[Double]= its match {
        case 0 => acc
        case _ => {
          val x = Gaussian(0.1, 0.2).cdf(Uniform(0, 1).sample)*data.get(root).bcdurext
          mc(its-1, x :: acc)
        }
      }

      val res = mc(runs, Nil)
      println("res.max:" +res.max)
      println("res.min:" +res.min)
      println("mean: "+ (res.max - res.min)/runs)
    }
  }
}
