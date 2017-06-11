package com.montecarlo

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.layout.{BorderPane,VBox}

object ProjectData{
  val PROJECT_NAME = "JRP"
  val RUN_NB = "001"
  val SCENARIO_SEQ = Seq("All Options", "Option 1", "Option 2a (Europe)", "Option 2a (Singapore)", "Option 2c (Europe)", "Option 2c (Singapore)")
}

trait Views extends GanttView with OutputsView with SimuControllerView with ManagerView with GeneralView{ this: MVC =>
  
  class Views {
    
    val rootPane = new BorderPane
    
    val outputs = new OutputsView()
    val gantt = new GanttView()
    val simucontroller = new SimuControllerView()
    val manager = new ManagerView()
    val general = new GeneralView()

    var vbox_right = new VBox(){
      padding = Insets(1)
      spacing = 5
      children = List(
        general.vbox_mcgen,
        manager.managerpanel,
        simucontroller.simuctrpanel
      )
    }

    var vbox_left = new VBox(){
      padding = Insets(1)
      spacing = 5
      children = List(
        gantt.ganttpanel,
        outputs.outputspanel
      )
    }
    rootPane.top = general.menubar
    rootPane.left = vbox_left
    rootPane.right = vbox_right
  }
}