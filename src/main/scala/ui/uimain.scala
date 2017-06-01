package ui

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

import ui.viewer.Ganttpanel
import ui.outputs.Outputspanel
import ui.manager.Managerpanel
import ui.controller.Controllerpanel
import ui.templates.{TempTextFieldIndic}
import ui.templates.Templates
import ui.dependencies.Dependencies
import project.constants.ProjectData
import scala.io.Source


object uimain extends JFXApp{

	
	stage = new PrimaryStage{
	
		title = "Monte Carlo SW - Demo01"
		width = 1050
		height = 780
		
		val imageicon = new Image(Dependencies.pathbppicon,requestedWidth = 120, requestedHeight = 120, preserveRatio = true, smooth = true)
		icons += imageicon
		
		centerOnScreen()
		
		scene = new Scene {
			
			var viewer = new SwingNode()
			
			createSwingContent(viewer)
			
			val mctitle = new Label("Monte Carlo Simulator"){font = Templates.titlefont}
			val mcversion = new Label("Demo 01 - version v01.00")
			
			val mclabjct = new Label("Project")
			val mcpjtname = new TempTextFieldIndic(ProjectData.PROJECT_NAME)
			
			val mclabrunnb = new Label("Run #")
			val mcrunnb= new TempTextFieldIndic(ProjectData.RUN_NB)
			
			var bppicon = new ImageView(imageicon)
			
			var vbox_MCTitleVersion = new VBox(){padding = Insets(5)}
			vbox_MCTitleVersion.setSpacing(1)
			vbox_MCTitleVersion.getChildren().addAll(mctitle,mcversion)
			

			var hbox_top = new HBox(){padding = Insets(5)}
			hbox_top.setSpacing(40)
			hbox_top.getChildren().addAll(vbox_MCTitleVersion, bppicon)
			
			var hbox_prj = new HBox(){padding = Insets(5)}
			hbox_prj.setSpacing(2)
			hbox_prj.setAlignment(Pos.CENTER)
			hbox_prj.getChildren().addAll(mclabjct, mcpjtname)

			var hbox_runnb = new HBox(){padding = Insets(5)}
			hbox_runnb.setSpacing(2)
			hbox_runnb.setAlignment(Pos.CENTER)
			hbox_runnb.getChildren().addAll(mclabrunnb, mcrunnb)
			
			var vbox_bottomleft = new VBox(){padding = Insets(5)}
			vbox_bottomleft.setSpacing(5)
			vbox_bottomleft.setAlignment(Pos.CENTER_RIGHT)
			vbox_bottomleft.getChildren().addAll(hbox_prj, hbox_runnb)
			
			
			var mclabtimedate = new Label("Time & Date")
			var mctimedate = new TempTextFieldIndic("23:01 \n29/05/2017")
			
			var vbox_bottomrigth = new VBox(){padding = Insets(1)}
			vbox_bottomrigth.setAlignment(Pos.CENTER)
			vbox_bottomrigth.setSpacing(5)
			vbox_bottomrigth.getChildren().addAll(mclabtimedate, mctimedate)
			
			var hbox_bottom = new HBox(){padding = Insets(1)}
			hbox_bottom.setSpacing(120)
			hbox_bottom.setAlignment(Pos.BOTTOM_LEFT)
			hbox_bottom.getChildren().addAll(vbox_bottomleft, vbox_bottomrigth)
			
			var vbox_mcgen = new VBox(){padding = Insets(1)}
			vbox_mcgen.getChildren().addAll(hbox_top,hbox_bottom)
			vbox_mcgen.border = new Border(new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.SOLID,CornerRadii.EMPTY, BorderWidths.DEFAULT))
			
			
			var vbox_right = new VBox(){padding = Insets(1)}
			vbox_right.setSpacing(5)
			vbox_right.getChildren().addAll(vbox_mcgen,Managerpanel.manager,Controllerpanel.controller)
			
			var vbox_left = new VBox(){padding = Insets(1)}
			vbox_left.setSpacing(5)
			vbox_left.getChildren().addAll(viewer, Outputspanel.outputs_viewer)
			
			
			var menubar = new MenuBar()
			var filemenu = new Menu("File")
			var helpmenu = new Menu("Help")

			var itemexit = new MenuItem("Exit")
			var itemhelp = new MenuItem("About Monte Carlo SW")
			
			filemenu.items = List(itemexit)
			helpmenu.items = List(itemhelp)
			menubar.menus = List(filemenu, helpmenu)
			val rootPane = new BorderPane
			
			rootPane.top = menubar
			rootPane.left = vbox_left
			rootPane.right = vbox_right
			root = rootPane
			
		}
		

	def createSwingContent(node:SwingNode){

		SwingUtilities.invokeLater(new Runnable{
			def run(){
				node.setContent(Ganttpanel.gantt)
				//viewer = Ganttpanel.gantt
			}
		})
	}
	}

}
