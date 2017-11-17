package com.montecarlo.model
import com.montecarlo.mvc.MVC

trait Models extends Database with GanttModel with BusinessModel {this: MVC =>

  class Model {

    val database = new Database()
    val ganttmodel = new GanttModel()
    val businessmodel = new BusinessModel()

    /**
     * run the montecarlo simulation
     */
    def runMonteCarlo(scenario:String, numberofrun: Int) {
      database.loadIO(scenario)
      val (graph, costs, durs) = businessmodel.mcSimulator(numberofrun, database.readDBGraph)
      database.updateDBGraph(graph)
      database.updateDBCosts(costs)
      database.updateDBDurations(durs)
      database.extractIO(scenario)
    }
  }
}
