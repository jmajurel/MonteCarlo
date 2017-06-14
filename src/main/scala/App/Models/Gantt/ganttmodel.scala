package com.montecarlo

trait GanttModel { this: Models =>
  
  class GanttModel{
    var scenario = new Scenario(id=1, name="scenario001", path="C:")
    //scenario.load()
    var scenarios:String ="sceneriolist"
    //var scenarios : Array[Scenario]
  }
  object GanttData{
    
    
    
  }
}