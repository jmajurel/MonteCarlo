package com.montecarlo.controller

import com.montecarlo.mvc.MVC

trait Controllers {this: MVC =>

  class Controller { 

    /**
     * Send message to the model to run mc simulation
     */
    def actionRun { 
      //val scenario = "MC_Ops_b4_tow_dev_test_BPP"
      //val scenario = "Scenario_Operations_Overview_DEV"
      //val scenario = "2a_SING_Scala"
      //val scenario = "Test_25-10-17"
      val scenario = "MCInputFile"
      val nbofrun = 100000
      model.runMonteCarlo(scenario, nbofrun)
    }
  }
}
