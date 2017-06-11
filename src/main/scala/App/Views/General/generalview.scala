package com.montecarlo

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.control.{Button,Label,TabPane,Tab,CheckBox,MenuBar,Menu,MenuItem,TextField}
import scalafx.scene.layout.{BorderPane,VBox,HBox}
import scalafx.scene.text.{Font, FontWeight, Text}
import scalafx.embed.swing.SwingNode
import scalafx.geometry.Pos

import java.awt.Dimension
import javafx.scene.layout.{Border,BorderStroke,CornerRadii,BorderWidths,BorderStrokeStyle}
import javax.swing.SwingUtilities
import scalafx.scene.paint.{Color}

trait GeneralView { this: Views =>

  class GeneralView {
    var menubar = new MenuBar()
    var filemenu = new Menu("File")
    var helpmenu = new Menu("Help")
    var itemexit = new MenuItem("Exit")
    var itemhelp = new MenuItem("About Monte Carlo SW")
    
    filemenu.items = List(itemexit)
    helpmenu.items = List(itemhelp)
    menubar.menus = List(filemenu, helpmenu)
    
    
    val mctitle = new Label("Monte Carlo Simulator"){
      font = Templates.titlefont
    }
    val mcversion = new Label("Demo 01 - version v01.00")
    val mclabjct = new Label("Project")
    val mcpjtname = new TempTextFieldIndic(ProjectData.PROJECT_NAME)
    val mclabrunnb = new Label("Run #")
    val mcrunnb= new TempTextFieldIndic(ProjectData.RUN_NB)
    var bppicon = new ImageView(Main.imageicon)
    var hbox_top = new HBox(){
      padding = Insets(5)
      spacing = 40
      children = List(
        new VBox(){
          padding = Insets(5)
          spacing = 1
          children = List(
            mctitle,
            mcversion
          )
        },
        bppicon
      )
    }

    var hbox_runnb = new HBox(){
      padding = Insets(5)
      spacing = 2
      alignment = Pos.CENTER
      children = List(
        mclabrunnb,
        mcrunnb
      )
    }

    var vbox_bottomleft = new VBox(){
      padding = Insets(5)
      spacing = 5
      alignment = Pos.CENTER_RIGHT
      children = List(
        new HBox(){
          padding = Insets(5)
          spacing = 2
          alignment = Pos.CENTER
          children = List(
            mclabjct,
            mcpjtname
          )
        },
        hbox_runnb
      )
    }

    var mclabtimedate = new Label("Time & Date")
    var mctimedate = new TempTextFieldIndic("23:01 \n29/05/2017")
    var vbox_bottomrigth = new VBox(){
      padding = Insets(1)
      spacing = 5
      alignment = Pos.CENTER
      children = List(
        mclabtimedate,
        mctimedate
      )
    }

    var vbox_mcgen = new VBox(){
      padding = Insets(1)
      border = new Border(new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.SOLID,CornerRadii.EMPTY, BorderWidths.DEFAULT))
      children = List(
        hbox_top,
        new HBox(){
          padding = Insets(1)
          spacing = 120
          alignment = Pos.BOTTOM_LEFT
          children = List(
            vbox_bottomleft,
            vbox_bottomrigth
          )
        }
      )
    }
  }
}