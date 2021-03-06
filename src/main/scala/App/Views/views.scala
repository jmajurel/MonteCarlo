package com.montecarlo.view

import com.montecarlo.mvc.MVC

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.geometry.Insets
import scalafx.scene.layout.{BorderPane,VBox}


trait Views extends GanttView with OutputsView with SimuControllerView with ManagerView with GeneralView {this: MVC =>

  class View {

    //controller.actionRun 
    def init(): BorderPane={

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
      rootPane
    }
  }
}
