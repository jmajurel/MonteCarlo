package ui.manager

import scalafx.Includes._
import scalafx.scene.control.{TabPane,Tab,CheckBox,TextField,Label}
import scalafx.scene.layout.{BorderPane,HBox,VBox}
import scalafx.geometry.Pos
import scalafx.geometry.Insets

import scalafx.scene.shape.Rectangle
import ui.templates.{TempTextFieldCtr, TempTabPane}

object Managerpanel{

	val manager = new TempTabPane()
	val tab_setup = new Tab{ closable=false }
	val tab_op = new Tab{ closable=false }
	
	val lab_txtfprj = new Label ("Project")
	val txtfprj = new TempTextFieldCtr("JRP")
	
	val lab_txtfrunnb = new Label ("Run #")
	val txtfrunnb = new TempTextFieldCtr("001")
	
	val lab_srtdate = new Label ("Start Date")
	val txtfsrtdate = new TempTextFieldCtr("11/12/2015")
	
	val lab_nbofrun = new Label ("Number of Runs")
	val txtfnbofruns = new TempTextFieldCtr("100")
	
	val lab_options = new Label ("Options:")
	val chkOp1 = new CheckBox("Option1")
	val chkOp2a_eu = new CheckBox("Option2a Europe")
	val chkOp2a_sg = new CheckBox("Option2a Singapore")
	val chkOp2c_eu = new CheckBox("Option2c Europe")
	val chkOp2c_sg = new CheckBox("Option2c Singapore")
	
	val vbox_pjt = new VBox(){padding = Insets(2)}
	vbox_pjt.setSpacing(1)
	vbox_pjt.getChildren().addAll(lab_txtfprj, txtfprj)
	
	val vbox_nbofrun = new VBox(){padding = Insets(2)}
	vbox_nbofrun.setSpacing(1)
	vbox_nbofrun.getChildren().addAll(lab_nbofrun, txtfnbofruns)
	
	val vbox_startdate = new VBox(){padding = Insets(2)}
	vbox_startdate.setSpacing(1)
	vbox_startdate.getChildren().addAll(lab_srtdate, txtfsrtdate)
	
	val hbox_top = new HBox(){padding = Insets(2)}
	hbox_top.setSpacing(20)
	hbox_top.getChildren().addAll(vbox_pjt ,vbox_nbofrun,vbox_startdate)
	
	val vbox_topcenter = new VBox(){padding = Insets(2)}
	vbox_topcenter.setSpacing(5)
	vbox_topcenter.setAlignment(Pos.CENTER_LEFT)
	vbox_topcenter.getChildren().addAll(lab_txtfrunnb, txtfrunnb)
	

	
	val hbox_optionrow1 = new HBox(){padding = Insets(2)}
	hbox_optionrow1.setSpacing(85)
	hbox_optionrow1.setAlignment(Pos.CENTER_LEFT)
	hbox_optionrow1.getChildren().addAll(chkOp1, chkOp2c_eu)
	
	val hbox_optionrow2 = new HBox(){padding = Insets(2)}
	hbox_optionrow2.setSpacing(42)
	hbox_optionrow2.setAlignment(Pos.CENTER_LEFT)
	hbox_optionrow2.getChildren().addAll(chkOp2a_eu, chkOp2c_sg)
	
	
	
	val vbox_bottomcenter = new VBox(){padding = Insets(5)}
	vbox_bottomcenter.setSpacing(10)
	vbox_bottomcenter.getChildren().addAll(lab_options, hbox_optionrow1, hbox_optionrow2,chkOp2a_sg)
	vbox_bottomcenter.setAlignment(Pos.CENTER_LEFT)

	
	val vhbox_setup = new VBox(){padding = Insets(2)}
	vhbox_setup.setSpacing(15)
	vhbox_setup.setAlignment(Pos.BOTTOM_CENTER)
	vhbox_setup.getChildren().addAll(hbox_top, vbox_topcenter, vbox_bottomcenter)

	
	tab_setup.text = "Setup"
	
	tab_setup.content = vhbox_setup
	
	tab_op.text = "Operation"
	
	manager.tabs = List(tab_setup, tab_op)
	
}
