package com.montecarlo

trait Controllers { mvc: MVC =>

  class Controller { 

    /**
     * Send message to the model to run mc simulation
     */
    def actionRun { 
      //val scenario = "MC_Ops_b4_tow_dev_test_BPP"
      val scenario = "Scenario_Operations_Overview_DEV"
      val nbofrun = 100000
      model.runMonteCarlo(scenario, nbofrun)
    }
  }
}
