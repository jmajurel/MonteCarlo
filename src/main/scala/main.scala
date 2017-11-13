package com.montecarlo

import com.montecarlo.mvc.MVC
import com.montecarlo.view.Dependencies

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.image.Image

class MonteCarloApp extends JFXApp {

  var mvc = new MVC
  val imageicon = new Image(Dependencies.pathbppicon, requestedWidth = 125, requestedHeight = 125, preserveRatio = true, smooth = true)
  
  stage = new JFXApp.PrimaryStage {
    title.value = "Monte Carlo SW - Demo01"
    width = 1100
    height = 800
    icons += imageicon	
    centerOnScreen()
    scene = new Scene {
      root = mvc.view.init
    }
  }
}

object Main {
  def main(args: Array[String]){
    val app = new MonteCarloApp
    app.main(args)
  }
}
