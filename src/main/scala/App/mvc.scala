package com.montecarlo

import akka.actor.{ActorRef,ActorSystem}

class MVC extends Views with Models with Controllers{

  val system:ActorSystem = ActorSystem("mvc")
  val view = new View()
  val model = new Model()
  val controller = new Controller()
  system.terminate()
}
