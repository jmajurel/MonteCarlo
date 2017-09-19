package com.montecarlo


trait Models extends Database with GanttModel with BusinessModel { this: MVC =>

  class Model {

    val database = new Database()
    val ganttmodel = new GanttModel()
    val businessmodel = new BusinessModel()

    /**
     * run the montecarlo simulation
     */
    def runMonteCarlo(scenario:String, numberofrun: Int) {
      database.loadIO(scenario)
      database.writeDB(businessmodel.mcSimulator(numberofrun, database.readDB))
      //database.writeDB(businessmodel.statistic(scenario, database.readDB))
      database.extractIO(scenario)
    }
  }
}
