package com.montecarlo

trait Controllers { mvc: MVC =>

  class Controller { 
    def actionRun { 
      model.loadData("Scenario_Operations_Overview_DEV.xlsx")
      model.displayData
    }
  }
}
