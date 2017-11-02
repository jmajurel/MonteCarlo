package com.montecarlo
import scala.math.exp
import scala.math.pow
import scala.math.log

trait PDFunctions {this: BusinessModel =>

  /**
   * perform the inverse pareto calculation
   */
  case class BPPareto(mu: Double, sigma: Double){
    def inverseCdf(y: Double) = mu/pow((1-y), (1/sigma)) 
  }

  /**
   * perform inverse extreme value distribution
   */
  case class BPPExtreme(alpha: Double, beta: Double){
    def inverseCdf(y: Double) = alpha - (beta * log((-1)*log(y)))
  }
}
