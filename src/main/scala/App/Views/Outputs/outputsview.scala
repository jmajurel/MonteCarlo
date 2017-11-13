package com.montecarlo.view

import com.montecarlo.model.{ProjectData, Estimations}

import scalafx.geometry.Insets
import scalafx.Includes._
import scalafx.scene.control.{TabPane,Tab,ChoiceBox,Label,SingleSelectionModel}
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.{GridPane,HBox,VBox}
import scalafx.geometry.Pos
import scalafx.scene.chart.LineChart
import scalafx.scene.chart.NumberAxis
import scalafx.scene.chart.XYChart

trait OutputsView { this: Views =>

  class OutputsView {
    val outputspanel = new TempTabPane()
    val tab_ts = new Tab{ closable=false }
    val tab_outcharts = new Tab{ closable=false }
    val tab_outres = new Tab{ closable=false }
    
    tab_ts.text = "Estimations"
    tab_outcharts.text = "Output Charts"
    tab_outres.text = "Output Results"
    outputspanel.tabs += tab_ts
    outputspanel.tabs += tab_outcharts
    outputspanel.tabs += tab_outres
    
    val labcombobox = new Label("Scenario:")
    val choicelist = new ObservableBuffer[String]
    choicelist ++= ProjectData.SCENARIO_SEQ
    val scenchoicebox = new ChoiceBox(choicelist)
    scenchoicebox.value = ProjectData.SCENARIO_SEQ(2)
    
    val hbox_scen= new HBox{
      spacing = 5
      children = List(
          labcombobox,
          scenchoicebox
      )
    }
   
    val xAxis = NumberAxis("Days", 0, 2000, 200)
    val yAxis = NumberAxis("Cost Estimation (M$)", 0, 200, 20)
    val toChartData = (xy: (Double, Double)) => XYChart.Data[Number, Number](xy._1, xy._2/1000000)
    val toChartDatabpp = (xy: (Double, Double)) => XYChart.Data[Number, Number](xy._1, (xy._2/1000000) -((xy._2/1000000)*0.13))
    val toChartDatamc = (xy: (Double, Double)) => XYChart.Data[Number, Number](xy._1, (xy._2/1000000) -((xy._2/1000000)*0.09))
    
    val crondallserie = new XYChart.Series[Number, Number] {
      name = "Crondall"
      data = Estimations.basecrondallesti.map(toChartData)
    }
    
    val bppserie = new XYChart.Series[Number, Number] {
      name = "BPP"
      data = Estimations.basecrondallesti.map(toChartDatabpp)
    }
    
    val mcserie = new XYChart.Series[Number, Number] {
      name = "MC"
      data = Estimations.basecrondallesti.map(toChartDatamc)
    }    

     var chart = new LineChart[Number, Number](xAxis, yAxis, ObservableBuffer(crondallserie,bppserie,mcserie)){
      legendVisible= true
      minWidth = 650
      maxWidth = 650
      minHeight = 350
      maxHeight = 350
      createSymbols = false
    }

    chart.getStylesheets().add("colored-chart.css")

    chart.resize(550,700)
    val vbox_ts= new VBox(){
      padding = Insets(20)
      //alignment = Pos.CenterLeft
      children = List(
          hbox_scen,
          chart
      )
    }

    tab_ts.content=vbox_ts
  }
}
