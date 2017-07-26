package com.montecarlo

trait Controllers { mvc: MVC =>

  class Controller { 
    def actionRun { 
      model.load()
    }
  }
}
