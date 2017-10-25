package com.montecarlo

import scala.math.exp
import scala.math.pow

trait PDFunctions {this: BusinessModel =>

  /**
   * perform the inverse pareto calculation
   */
  case class BPPareto(mu: Double, sigma: Double){
    def inverseCdf(y: Double) = mu/pow((1-y), (1/sigma)) 
  }
}
