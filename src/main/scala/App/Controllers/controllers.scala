package com.montecarlo
import akka.actor.{Actor, Props}

trait Controllers { mvc: MVC =>
  object Controller{
    def props(message: String, viewActor: ActorRef): Props = Props(new Controller(message, viewActor)) 
  }
  class Controller extends Actor{ 
    println("Hi Guys I am the Controller")
    def receive = {



    }
  }
}
