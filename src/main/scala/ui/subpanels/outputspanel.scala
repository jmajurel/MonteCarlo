package ui.outputs
import javafx.geometry.Insets


import scalafx.Includes._
import scalafx.scene.control.{TabPane,Tab,ComboBox,Label}
import ui.templates.{TempTabPane,TempTextFieldIndic}
import scalafx.scene.layout.{GridPane,HBox}


object Outputspanel{

	val outputs_viewer = new TempTabPane()
	val tab_ts = new Tab{ closable=false }
	val tab_outcharts = new Tab{ closable=false }
	val tab_outres = new Tab{ closable=false }
	
	tab_ts.text = "Time Series"
	tab_outcharts.text = "Output Charts"
	tab_outres.text = "Output Results"
	
	outputs_viewer.tabs += tab_ts
	outputs_viewer.tabs += tab_outcharts
	outputs_viewer.tabs += tab_outres
	
	
	var gridts = new GridPane()
	
	gridts.gridLinesVisible = true
	gridts.vgap = 20
	gridts.hgap = 20
	gridts.setPadding(new Insets(0, 20, 0, 20))
	
	val labcombobox = new Label("Sceneario:")
	val scencombobox = new ComboBox(Seq("All options","test"))
	
	val labcostlimit = new Label("Cost Limit:")
	val textfield = new TempTextFieldIndic("0")

	
	val hbox_costlimit= new HBox()
	hbox_costlimit.setSpacing(1)
	hbox_costlimit.getChildren().addAll(labcostlimit,textfield)
	
	val hbox_scen= new HBox()
	hbox_scen.setSpacing(1)
	hbox_scen.getChildren().addAll(labcombobox,scencombobox)
	
	tab_ts.content=gridts
	gridts.add(hbox_scen, 1, 1)
	gridts.add(hbox_costlimit, 10, 1)

	
}
