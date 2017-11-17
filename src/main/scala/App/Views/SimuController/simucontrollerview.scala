package com.montecarlo.view

import com.montecarlo.mvc.MVC

import scalafx.Includes._
import scalafx.scene.control.{TextField,Label,Button,Slider}
import scalafx.scene.layout.{HBox,VBox}
import scalafx.scene.text.{Font, FontWeight, Text}
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.event.{EventHandler, ActionEvent}

trait SimuControllerView {this: MVC =>

  class SimuControllerView {

    val simuctrpanel = new TempBorderPane()
    val labctr = new Label("Simulator Controller"){font = Font.font(null, FontWeight.Bold, 13)}
    val lab_elaptime = new Label("Elapsed Time (hrs):")
    val elaptime = new TempTextFieldIndic("0")
    val hbox_elaptime= new HBox(){
      padding = Insets(5)
      spacing = 15
      alignment = Pos.CenterRight
      children = List(
          lab_elaptime,
          elaptime
      )
    }

    
    val imageplayicon = new Image(Dependencies.pathplayicon,requestedWidth = 45, requestedHeight = 45, preserveRatio = true, smooth = true)
    val imagestopicon = new Image(Dependencies.pathstopicon,requestedWidth = 45, requestedHeight = 45, preserveRatio = true, smooth = false)
    val imagepauseicon = new Image(Dependencies.pathpauseicon,requestedWidth = 45, requestedHeight = 45, preserveRatio = true, smooth = false)
    val play_butt = new Button("",new ImageView(imageplayicon))
    play_butt.onAction = handle{controller.actionRun}
    val stop_butt = new Button("",new ImageView(imagestopicon))
    val pause_butt = new Button("",new ImageView(imagepauseicon))
    
    val hbox_playerclt= new HBox(){
      padding = Insets(5)
      spacing = 40
      alignment = Pos.CenterRight
      children = List(
          pause_butt,
          stop_butt,
          play_butt
      )
    }

    var slidetimeacc = new Slider(0,100,100){
      showTickLabels=true
      minWidth=300
      maxWidth=300
    }

    val vbox_simuclt = new VBox(){
      padding = Insets(20)
      spacing = 10
      alignment =Pos.CenterLeft
      children = List(
          labctr,
          hbox_elaptime,
          slidetimeacc,
          hbox_playerclt
      )
    }
    
    simuctrpanel.left = vbox_simuclt
  }
}
