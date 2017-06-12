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
import scalafx.scene.paint.Color

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
    
    val bppimg = new Image(Dependencies.pathbppicon, requestedWidth = 125, requestedHeight = 125, preserveRatio = true, smooth = true)
    val mctitle = new Label("Monte Carlo Simulator"){
      font = Templates.titlefont
    }
    val mcversion = new Label("version v02.01")
    val mclabjct = new Label("BPP Project:")
    val mcpjtname = new TempTextFieldIndic(ProjectData.BPPPROJECT){maxWidth = 140}

    val mclabcustm = new Label("Customer:")
    val mccustom= new TempTextFieldIndic(ProjectData.CUSTOMERNAME){maxWidth = 140}
    val mclabruntitle = new Label("Title:")
    val mcruntitle= new TempTextFieldIndic("Test run 001"){maxWidth = 140}
    
    var bppicon = new ImageView(bppimg)
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
    var hbox_prjt = new HBox(){
      padding = Insets(5)
      spacing = 2
      alignment = Pos.BottomRight
      children = List(
        mclabjct,
        mcpjtname
       )
    }
    var hbox_custom = new HBox(){
      padding = Insets(5)
      spacing = 2
      alignment = Pos.BottomRight
      children = List(
        mclabcustm,
        mccustom
      )
    }

    var hbox_runtitle = new HBox(){
      padding = Insets(5)
      spacing = 2
      alignment = Pos.BottomRight
      children = List(
        mclabruntitle,
        mcruntitle
      )
    }    
    var vbox_bottomleft = new VBox(){
      padding = Insets(5)
      spacing = 2
      alignment = Pos.BottomRight
      children = List(
        hbox_prjt,
        hbox_custom,
        hbox_runtitle
      )
    }

    var mclabtimedate = new Label("Run Time/Date")
    var mctimedate = new TempTextFieldIndic("23:01 \n29/05/2017"){
      maxWidth = 110
    }
    var vbox_bottomrigth = new VBox(){
      padding = Insets(1)
      spacing = 5
      alignment = Pos.Center
      children = List(
        mclabtimedate,
        mctimedate
      )
    }

    var vbox_mcgen = new VBox(){
      padding = Insets(1)
      border = new Border(new BorderStroke(Color.LightGrey, BorderStrokeStyle.SOLID,CornerRadii.EMPTY, BorderWidths.DEFAULT))
      children = List(
        hbox_top,
        new HBox(){
          padding = Insets(1)
          spacing = 50
          //alignment = Pos.BOTTOM_LEFT
          children = List(
            vbox_bottomleft,
            vbox_bottomrigth
          )
        }
      )
    }
  }
}