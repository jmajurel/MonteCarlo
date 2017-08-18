package com.montecarlo

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import breeze.stats.distributions.{LogNormal, Gaussian, Uniform}
import breeze.linalg.randomDouble
import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._
import scalax.collection.GraphTraversal.Successors

trait MonteCarlo {this: Models =>

  object MonteCarlo {

      def simulator(runs: Int) {
        println("welcome in the simulator") 

/*        @tailrec
        def mc(its: Int) {
          if (its > 0) {
            val traverser = (graphdata get root).outerNodeTraverser.withDirection(Successors)
            traverser.foreach((node: Operation) => {

              val res = (Gaussian(0.1, 0.2).cdf(Uniform(0,1).sample) * node.bcdurbpp)
              node.mcresdur = node.mcresdur :+ res 

              if(node.bconeoffcostbpp.isDefined)
                node.mcrescost = node.mcrescost :+ (Gaussian(0.1, 0.2).cdf(Uniform(0,1).sample) * node.bconeoffcostbpp.getOrElse(0.0))
              else if(node.bcdayratebpp.isDefined)
                node.mcrescost = node.mcrescost :+ (Gaussian(0.1, 0.2).cdf(Uniform(0,1).sample) * node.bcdayratebpp.getOrElse(0.0) * res)
              } 
            )
            mc(its-1)
          }
        }node*/

       @tailrec
       def mc(its: Int) {
         if(its >0) {
           val mylist = (graphdata get root).outerNodeTraverser.withDirection(Successors).toList

           @tailrec
           def calc(listofnode: List[Operation]):Int = {
             listofnode match {
               case node::rest => {
                val res = (Gaussian(0.1, 0.2).cdf(Uniform(0,1).sample) * node.bcdurbpp)
                node.mcresdur = node.mcresdur :+ res 

                if(node.bconeoffcostbpp.isDefined)
                  node.mcrescost = node.mcrescost :+ (Gaussian(0.1, 0.2).cdf(Uniform(0,1).sample) * node.bconeoffcostbpp.getOrElse(0.0))
                else if(node.bcdayratebpp.isDefined)
                  node.mcrescost = node.mcrescost :+ (Gaussian(0.1, 0.2).cdf(Uniform(0,1).sample) * node.bcdayratebpp.getOrElse(0.0) * res)
                 calc(rest)
               }
               case Nil => 0
             }
           }
        calc(mylist)
        mc(its-1)
      }
    }
    mc(runs)
  }
}
}
