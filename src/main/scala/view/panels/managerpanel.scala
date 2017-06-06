package view.manager

import scalafx.Includes._
import scalafx.scene.control.{TabPane,Tab,CheckBox,TextField,Label,ChoiceBox}
import scalafx.scene.layout.{BorderPane,HBox,VBox}
import scalafx.geometry.Pos
import scalafx.geometry.Insets
import scalafx.scene.paint.{Color}
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.{Font, FontWeight, Text}
import scalafx.collections.ObservableBuffer


import view.templates.{TempTextFieldCtr, TempTabPane}
import project.constants.ProjectData

object Managerpanel{

	val manager = new TempTabPane()
	val tab_setup = new Tab{closable=false }
	val tab_op = new Tab{closable=false }
	
	
	val lab_txtfprj = new Label ("Project")
	var txtfprj = new TempTextFieldCtr(ProjectData.PROJECT_NAME)
	
	val lab_txtfrunnb = new Label ("Run #")
	var txtfrunnb = new TempTextFieldCtr(ProjectData.RUN_NB)
	
	val lab_srtdate = new Label ("Start Date")
	var txtfsrtdate = new TempTextFieldCtr("11/12/2015")
	
	val lab_nbofrun = new Label ("Number of Runs")
	var txtfnbofruns = new TempTextFieldCtr("100")
	
	val lab_options = new Label ("Options:")
	var chkOp1 = new CheckBox("Option1"){
		selected = true
	}
	var chkOp2a_eu = new CheckBox("Option2a Europe")
	var chkOp2a_sg = new CheckBox("Option2a Singapore")
	var chkOp2c_eu = new CheckBox("Option2c Europe")
	var chkOp2c_sg = new CheckBox("Option2c Singapore")
	
	val vbox_pjt = new VBox(){
		spacing = 5
	}
	vbox_pjt.getChildren().addAll(lab_txtfprj, txtfprj)
	
	val vbox_nbofrun = new VBox(){
		spacing = 5
	}
	
	vbox_nbofrun.getChildren().addAll(lab_nbofrun, txtfnbofruns)
	
	val vbox_startdate = new VBox(){
		spacing = 5
	}

	vbox_startdate.getChildren().addAll(lab_srtdate, txtfsrtdate)
	
	val hbox_top = new HBox(){
		spacing = 25
	}
	hbox_top.getChildren().addAll(vbox_pjt ,vbox_nbofrun,vbox_startdate)
	
	val vbox_topcenter = new VBox(){
		spacing = 5
	}

	vbox_topcenter.getChildren().addAll(lab_txtfrunnb, txtfrunnb)
	

	val hbox_optionrow1 = new HBox(){
		spacing = 85
		alignment=Pos.CENTER_LEFT
	}

	hbox_optionrow1.getChildren().addAll(chkOp1, chkOp2c_eu)
	
	val hbox_optionrow2 = new HBox(){
		spacing = 39
		alignment=Pos.CENTER_LEFT
	}

	hbox_optionrow2.getChildren().addAll(chkOp2a_eu, chkOp2c_sg)
	
	
	
	val vbox_bottomcenter = new VBox(){
		spacing = 10
		alignment=Pos.CENTER_LEFT
	}

	vbox_bottomcenter.getChildren().addAll(lab_options, hbox_optionrow1, hbox_optionrow2,chkOp2a_sg)

	
	val vhbox_setup = new VBox(){
		padding = Insets(10,10,10,10)
		spacing = 12
		alignment=Pos.CENTER_LEFT
	}

	vhbox_setup.getChildren().addAll(hbox_top, vbox_topcenter, vbox_bottomcenter)

	
	tab_setup.text = "Setup"
	
	tab_setup.content = vhbox_setup
	

	
	
	val labscen = new Label("Scenario:")
	val scenachlist = new ObservableBuffer[String]
	scenachlist ++= ProjectData.SCENARIO_SEQ
	val scenchbox = new ChoiceBox(scenachlist)
	scenchbox.value = ProjectData.SCENARIO_SEQ(1)
	
	val hbox_scen = new HBox(){
		spacing = 2
		alignment=Pos.CENTER
	}
	
	hbox_scen.getChildren().addAll(labscen,scenchbox)
	
	
	val labesti = new Label("Estimation:")
	val estimlist = new ObservableBuffer[String]
	estimlist ++= Seq("Cost", "Time")
	val estichbox = new ChoiceBox(estimlist)
	estichbox.value = estimlist(0)

	val hbox_esti = new HBox(){
		spacing = 2
		alignment=Pos.CENTER
	}
	
	hbox_esti.getChildren().addAll(labesti,estichbox)
	
	val hbox_optop = new HBox(){
		padding = Insets(5,2,2,5)
		spacing = 60
		alignment=Pos.CENTER
	}
	
	hbox_optop.getChildren().addAll(hbox_scen,hbox_esti)
	
	tab_op.text = "Operation"
	tab_op.content = hbox_optop
	
	manager.tabs = List(tab_setup, tab_op)
	
}
