package com.montecarlo

import scalafx.Includes._
import scalafx.scene.control.{TabPane,Tab,CheckBox,TextField,Label,ChoiceBox}
import scalafx.scene.layout.{BorderPane,HBox,VBox,GridPane}
import scalafx.geometry.{Pos,Orientation,Insets}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.{Font,FontWeight,Text}
import scalafx.collections.ObservableBuffer

trait ManagerView { this: Views =>

  class ManagerView {
    val managerpanel = new TempTabPane()
    
    val tab_setup = new Tab{closable=false }
    val tab_slt = new Tab{closable=false }
    val lab_txtfprj = new Label ("Project")
    var txtfprj = new TempTextFieldCtr(ProjectData.BPPPROJECT)
    val lab_txtfrunnb = new Label ("Run #")
    var txtfrunnb = new TempTextFieldCtr(ProjectData.RUN_NB)
    val lab_srtdate = new Label ("Start Date")
    var txtfsrtdate = new TempTextFieldCtr("11/12/2015")
    val lab_nbofrun = new Label ("Number of Runs")
    var txtfnbofruns = new TempTextFieldCtr("100")
    val lab_options = new Label ("Options:")
    var chkallops = new CheckBox("All Options")
    var chkA = new CheckBox("A: Interim works"){
      selected = true
    }
    var chkB = new CheckBox("B: Option 1 - Convert to perm spread")
    var chkC = new CheckBox("C: Option 2a - Turret repair EU")
    var chkD = new CheckBox("D: Option 2a - Turret repair SING")
    var chkE = new CheckBox("E: Option 2c - Turret replace SING")
    var chkF = new CheckBox("F: Option 2c - Turret replace EU")   
    var chkG = new CheckBox("G: In-situ - Turret repair")
    val vbox_pjt = new VBox(){
      spacing = 5
    }
    vbox_pjt.getChildren().addAll(lab_txtfprj, txtfprj)
    
    val vbox_nbofrun = new VBox(){
      spacing = 5
      children = List(
        lab_nbofrun,
        txtfnbofruns
      )
    }
    val vbox_startdate = new VBox(){
      spacing = 5
      children = List(
        lab_srtdate,
        txtfsrtdate
      )
    }
    val hbox_top = new HBox(){
      spacing = 25
      children = List(
        vbox_pjt,
        vbox_nbofrun, 
        vbox_startdate
      )
    }
    val vbox_topcenter = new VBox(){
      spacing = 5
      children = List(
        lab_txtfrunnb,
        txtfrunnb
      )  
    }

    val vhbox_setup = new VBox(){
      padding = Insets(20,20,100,20)
      spacing = 12
      alignment=Pos.CenterLeft
      children = List(
        hbox_top,
        vbox_topcenter
      ) 
    }

    tab_setup.text = "Setup"
    tab_setup.content = vhbox_setup
    
    
    val gridselection = new GridPane(){
      hgap = 10
      vgap = 10
      padding = Insets(10,10,10,10)
    }
    gridselection.add(lab_options,1,1)
    gridselection.add(chkallops,1,2)
    gridselection.add(chkA,1,3)
    gridselection.add(chkB,1,4)
    gridselection.add(chkC,1,5)
    gridselection.add(chkD,1,6)
    gridselection.add(chkE,2,3)
    gridselection.add(chkF,2,4)
    gridselection.add(chkG,2,5)
    
    val labscen = new Label("Scenario:")
    val scenachlist = new ObservableBuffer[String]
    scenachlist ++= ProjectData.SCENARIO_SEQ
    val scenchbox = new ChoiceBox(scenachlist)
    scenchbox.value = ProjectData.SCENARIO_SEQ(1)
    val hbox_scen = new HBox(){
      spacing = 2
      alignment=Pos.Center
      children = List(
        labscen,
        scenchbox
      ) 
    }

    val labesti = new Label("Estimation:")
    val estimlist = new ObservableBuffer[String]
    estimlist ++= Seq("Cost", "Time")
    val estichbox = new ChoiceBox(estimlist)
    estichbox.value = estimlist(0)

    val hbox_esti = new HBox(){
      spacing = 2
      alignment=Pos.Center
      children = List(
        labesti,
        estichbox
      ) 
    }

    val hbox_optop = new HBox(){
      padding = Insets(5,2,2,5)
      spacing = 60
      alignment=Pos.Center
      children = List(
        hbox_scen,
        hbox_esti
      )   
    }

    tab_slt.text = "Selection"
    tab_slt.content = gridselection
    managerpanel.tabs = List(tab_setup, tab_slt)
  }
}