package view.controller

import scalafx.Includes._
import scalafx.scene.control.{TextField,Label,Button,Slider}
import scalafx.scene.layout.{HBox,VBox}
import scalafx.scene.text.{Font, FontWeight, Text}
import scalafx.geometry.Insets
import scalafx.geometry.Pos
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import view.dependencies.Dependencies
import view.templates.{TempBorderPane}


import view.templates.{TempTextFieldIndic, TempButton}

object Controllerpanel{ 
	val controller = new TempBorderPane()
	
	val labctr = new Label("Simulator Controller"){font = Font.font(null, FontWeight.Bold, 20)}
	
	val lab_simdate = new Label("Simulation Date:")
	val simdate = new TempTextFieldIndic("01/05/2017")
	
	val hbox_simdate= new HBox(){
		padding = Insets(5)
		spacing = 1
		alignment = Pos.CENTER_RIGHT
	}

	hbox_simdate.getChildren().addAll(lab_simdate,simdate)
	
	val lab_currop = new Label("Current Operation:")
	val currop = new TempTextFieldIndic("")
	
	val hbox_currop= new HBox(){
		padding = Insets(5)
		spacing = 1
		alignment = Pos.CENTER_RIGHT
	}

	hbox_currop.getChildren().addAll(lab_currop,currop)
	
	val lab_currrun = new Label("Current Run Number:")
	val currrun = new TempTextFieldIndic("")
	val lab_elaptime = new Label("Elapsed Time (hrs):")
	val elaptime = new TempTextFieldIndic("0")
	
	val hbox_currrun= new HBox(){
		padding = Insets(5)
		spacing = 1
		alignment = Pos.CENTER_RIGHT
	}

	hbox_currrun.getChildren().addAll(lab_elaptime, elaptime, lab_currrun,currrun)
	
	
	val imageplayicon = new Image(Dependencies.pathplayicon,requestedWidth = 40, requestedHeight = 40, preserveRatio = true, smooth = false)
	val imagestopicon = new Image(Dependencies.pathstopicon,requestedWidth = 40, requestedHeight = 40, preserveRatio = true, smooth = false)
	val imagepauseicon = new Image(Dependencies.pathpauseicon,requestedWidth = 40, requestedHeight = 40, preserveRatio = true, smooth = false)
	
	
	val play_butt = new Button("",new ImageView(imageplayicon))
	val stop_butt = new Button("",new ImageView(imagestopicon))
	val pause_butt = new Button("",new ImageView(imagepauseicon))
	
	val reload_butt = new TempButton("Reload")
	val stop_ext = new TempButton("Exit")
	
	val hbox_bottom= new HBox(){
		padding = Insets(5)
		spacing = 35
		alignment = Pos.CENTER_RIGHT
	}

	hbox_bottom.getChildren().addAll(pause_butt,stop_butt,play_butt,stop_ext)
	
	var slidetimeacc = new Slider(0,100,100){
	showTickLabels=true
	minWidth=250
	maxWidth=250
	}
	
	val hbox_middle= new HBox(){
		padding = Insets(5)
		spacing = 35
		alignment = Pos.CENTER_RIGHT
	}
	hbox_middle.getChildren().addAll(slidetimeacc, reload_butt)
	
	
	val vbox_rigth = new VBox(){
		padding = Insets(5)
		spacing = 5
		alignment =Pos.CENTER_RIGHT
	}

	vbox_rigth.getChildren().addAll(hbox_simdate, hbox_currop,hbox_currrun,hbox_middle,hbox_bottom)
	controller.right = vbox_rigth
	
}