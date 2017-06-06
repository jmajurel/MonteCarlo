package view.outputs
import scalafx.geometry.Insets


import scalafx.Includes._
import scalafx.scene.control.{TabPane,Tab,ChoiceBox,Label,SingleSelectionModel}
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.{GridPane,HBox,VBox}
import scalafx.geometry.Pos
import scalafx.scene.chart.LineChart
import scalafx.scene.chart.NumberAxis
import scalafx.scene.chart.XYChart


import view.templates.{TempTabPane,TempTextFieldIndic}
import project.constants.ProjectData

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
	
	
	val labcombobox = new Label("Scenario:")
	val choicelist = new ObservableBuffer[String]
	choicelist ++= ProjectData.SCENARIO_SEQ
	val scenchoicebox = new ChoiceBox(choicelist)
	val labcostlimit = new Label("Cost Limit:")
	val textfield = new TempTextFieldIndic("0")
	scenchoicebox.value = ProjectData.SCENARIO_SEQ(0)
	

	
	val hbox_scen= new HBox{
		spacing = 5
		alignment = Pos.CENTER
	}
	hbox_scen.getChildren().addAll(labcombobox,scenchoicebox)
	
	val hbox_costlimit= new HBox(){
		spacing = 5
		alignment = Pos.CENTER
	}
	hbox_costlimit.getChildren().addAll(labcostlimit,textfield)
	
	val hbox_top= new HBox(){
		spacing = 70
		alignment = Pos.CENTER
	}
	hbox_top.getChildren().addAll(hbox_scen,hbox_costlimit)
	
	
	val xAxis = NumberAxis("Duration (hrs)", 0, 100, 10)

	val yAxis = NumberAxis("Cost Estimation (USD)", 0, 10000, 1000)
	val toChartData = (xy: (Double, Double)) => XYChart.Data[Number, Number](xy._1, xy._2)

	val series1 = new XYChart.Series[Number, Number] {

	name = ""

	data = Seq(
		(0.0, 1.0),
		(10.0, 1000.0),
		(20.0, 3000.0),
		(30.0, 3000.0),
		(70.0, 7000.0),
		(100.0, 9000.0)).map(toChartData)
	}
	var chart = new LineChart[Number, Number](xAxis, yAxis, ObservableBuffer(series1)){
		legendVisible= false
		maxHeight = 350
		maxWidth = 600
	}
	chart.resize(180,30)

	val vbox_ts= new VBox(){
		spacing = 5
		padding = Insets(5,2,5,5)
		alignment = Pos.CENTER_LEFT
	}
	
	vbox_ts.getChildren().addAll(hbox_top,chart)
	
	tab_ts.content=vbox_ts
}
