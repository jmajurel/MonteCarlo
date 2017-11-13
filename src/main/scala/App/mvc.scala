package com.montecarlo.mvc

import com.montecarlo.model.Models
import com.montecarlo.view.Views
import com.montecarlo.controller.Controllers

class MVC extends Views with Models with Controllers {
  
  val model = new Model()
  val controller = new Controller()
  val view = new View()
}
