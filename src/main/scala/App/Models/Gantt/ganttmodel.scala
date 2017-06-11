package com.montecarlo

trait GanttModel { this: Models =>
  
  class GanttModel extends Scenario{
    var scenario = new Scenario()
    scenario.load()
    var scenarios:String ="sceneriolist"
    //var scenarios : Array[Scenario]
  }
}