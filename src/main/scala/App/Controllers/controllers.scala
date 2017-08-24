package com.montecarlo

trait Controllers { mvc: MVC =>

  class Controller { 
    def actionRun { 
      val filename = "MC_Ops_b4_tow_dev_test_BPP"
      model.loadData(filename)
      //model.displayData
      model.runMonteCarlo(filename, 100000)
      //model.geneOutputFile(filename)
    }
  }
}
