package com.montecarlo

class MVC extends Views with Models with Controllers {

  val model = new Model()
  val controller = new Controller()
  val view = new View()
}
