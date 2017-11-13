package com.montecarlo.model

trait GanttModel { this: Models =>
  
  class GanttModel{
    var scenario = new Scenario(id=1, name="scenario001", path="C:")
    var scenarios:String ="sceneriolist"
    //var scenarios : Array[Scenario]
  }
}
