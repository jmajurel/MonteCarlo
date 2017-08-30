package com.montecarlo

trait Controllers { mvc: MVC =>

  class Controller { 

    /**
     * Send message to the model to run mc simulation
     */
    def actionRun { 
      val scenario = "MC_Ops_b4_tow_dev_test_BPP"
      val nbofrun = 100000
      model.runMonteCarlo(scenario, nbofrun)
    }
  }
}
