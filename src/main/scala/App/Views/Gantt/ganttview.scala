package com.montecarlo

import java.util.Date
import java.util.Calendar
import java.util.GregorianCalendar
import java.text.SimpleDateFormat
import java.awt.Dimension
import java.awt.Color
import java.text.NumberFormat

import org.jfree.ui._
import org.jfree.data.gantt.Task
import org.jfree.data.gantt.TaskSeries
import org.jfree.data.gantt.TaskSeriesCollection
import org.jfree.data.time.SimpleTimePeriod
import org.jfree.data.category.IntervalCategoryDataset
import org.jfree.chart.JFreeChart
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.plot.CategoryPlot
import org.jfree.chart.axis.DateAxis
import org.jfree.chart.renderer.category.{GanttRenderer,BarRenderer,StandardBarPainter,CategoryItemRenderer}
import org.jfree.chart.labels.{CategoryItemLabelGenerator,StandardCategoryItemLabelGenerator,ItemLabelPosition,ItemLabelAnchor,IntervalCategoryItemLabelGenerator}
import org.jfree.data.category.CategoryDataset
import org.jfree.data.gantt.GanttCategoryDataset
import java.awt.Paint
import java.awt.Font
import scalafx.embed.swing.SwingNode

trait GanttView { this: Views =>
  
  class GanttView{
    
    var dataset = createDataset
    var ganttchart = createChart(dataset)
    val viewerdimension = new Dimension(100, 400)
    val lightblue = new Color(0, 176, 240)
    var taskscollection = new TaskSeriesCollection()
    configAxis(ganttchart)
    configGanttStyle(ganttchart)
    
    var viewer = new ChartPanel(ganttchart) 
    viewer.setPreferredSize(viewerdimension)
    //dataset.getSeries(0).get(2).setPercentComplete(0.1)
    
    var ganttpanel = new SwingNode()
    ganttpanel.setContent(viewer)
    
    def date( day : Int,  month: Int, year: Int):Date ={
      var calendar = Calendar.getInstance()
      calendar.set(year, month, day)
      var result = calendar.getTime()
      result
    }
  
    def configAxis(ganttchart : JFreeChart) = {
      var plot = new CategoryPlot()
      plot= ganttchart.getPlot().asInstanceOf[CategoryPlot]
      var valuesaxis = plot.getRangeAxis()
      valuesaxis.setLabelAngle(0.01)
      valuesaxis.setMinorTickCount(2)
      
      var axis = new DateAxis() 	  	
      axis = plot.getRangeAxis().asInstanceOf[DateAxis]
      axis.setDateFormatOverride(new SimpleDateFormat("MM/dd/YY"))
      
      //Configure Horizontal axis to timescale
      /*var axis = new DateAxis() 	  	
      axis = plot.getRangeAxis().asInstanceOf[DateAxis]
      axis.setDateFormatOverride(new SimpleDateFormat("MM/dd/YY"))
      axis.setLabelPaint(Color.lightGray)
      axis.setVerticalTickLabels(true)
      axis.setTickLabelFont(new Font(null, Font.PLAIN, 10))*/
    }
    
    def configGanttStyle(ganttchart : JFreeChart)={
      var plot = new CategoryPlot()
      plot= ganttchart.getPlot().asInstanceOf[CategoryPlot]
      plot.getRenderer().asInstanceOf[GanttRenderer].setShadowVisible(false)
      plot.getRenderer().asInstanceOf[BarRenderer].setShadowVisible(false)
      plot.getRenderer().asInstanceOf[BarRenderer].setBarPainter(new StandardBarPainter())
      plot.getRenderer().asInstanceOf[BarRenderer].setMaximumBarWidth(1)
      plot.getRenderer().asInstanceOf[BarRenderer].setItemMargin(0.001)
      plot.getRenderer().setPaint(lightblue)
      plot.getRenderer().setBaseOutlinePaint(lightblue)
      plot.getRenderer().setOutlinePaint(lightblue)
      plot.setBackgroundPaint(Color.white)
      plot.setRangeGridlinePaint(Color.gray)
      plot.setDomainGridlinePaint(Color.gray)
      //getEndPercent()
      
      //Display percentage of completion in tasks
     /* plot.getRenderer().asInstanceOf[CategoryItemRenderer].setBaseItemLabelGenerator( new StandardCategoryItemLabelGenerator("{1}",NumberFormat.getPercentInstance()) {
        override def  generateRowLabel( dataset: CategoryDataset, row: Int) :String = {
          return "Your Row Text  " + row;
        }
        override def generateColumnLabel( dataset:CategoryDataset, column: Int) : String = {
          return "Your Column Text  " + column;
        }
          override def generateLabel( dataset:CategoryDataset, row: Int, column: Int) :String = {
            var percentage = dataset.asInstanceOf[TaskSeriesCollection].getPercentComplete(row,column).asInstanceOf[Double]*100
            return percentage.toInt.toString + "%"
          }
      })
      //plot.getRenderer().asInstanceOf[CategoryItemRenderer].setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator())
      plot.getRenderer().asInstanceOf[CategoryItemRenderer].setBaseItemLabelsVisible(true)
      plot.getRenderer().asInstanceOf[CategoryItemRenderer].setBaseItemLabelPaint(Color.black)
      plot.getRenderer().asInstanceOf[CategoryItemRenderer].setBasePositiveItemLabelPosition(new ItemLabelPosition(
        ItemLabelAnchor.INSIDE6, 
        TextAnchor.BOTTOM_CENTER)
      )*/
    }
	
    /** Creates a new task
    *
    *  @param name its name
    *  @param startdate its start date
    *  @param enddate its end date
    */	
    def createTask(name:String, startdate: Date, enddate: Date) =
      new Task(name,new SimpleTimePeriod(startdate,enddate))

    def createTaskSeries(name:String) =
      new TaskSeries(name)

    def addTasktoTaskSeries(taskseries: TaskSeries, newTask: Task) ={
      taskseries.add(newTask)
    }

    def addTaskSeriesToCollect(taskcollect :TaskSeriesCollection, newtaskseries : TaskSeries) ={
      taskcollect.add(newtaskseries)
    }
    def createDataset :TaskSeriesCollection = {
      
      val bpptaskseries = new TaskSeries("BPP")
      val calendar = new GregorianCalendar()
      
      calendar.setTime(date(3, Calendar.APRIL, 2017))
      calendar.add(Calendar.DATE, 3)
      val enddate = calendar.getTime()
      
      var op1 = new Task(
        "ID 01-Sail to Site, DP trials",
        new SimpleTimePeriod( 
          date(3, Calendar.APRIL, 2017),
          date(4, Calendar.APRIL, 2017)
        )
      )
      var op2 = new Task(
        "ID 02-Seabed Survey",
        new SimpleTimePeriod( 
          date(4, Calendar.APRIL, 2017),
          date(6, Calendar.APRIL, 2017)
        )
      )
      var op3 = new Task(
        "ID 03-Geotechnical Survey",
        new SimpleTimePeriod( 
          date(4, Calendar.APRIL, 2017),
          date(7, Calendar.APRIL, 2017)
        )
      )
      var op4 = new Task(
        "ID 04-Sail to Port/ Demob",
        new SimpleTimePeriod( 
          date(7, Calendar.APRIL, 2017),
          date(9, Calendar.APRIL, 2017)
        )
      )
      var op5 = new Task(
        "ID 05-Hang-off fab.",
        new SimpleTimePeriod( 
          date(17, Calendar.JULY, 2018),
          date(1, Calendar.FEBRUARY, 2019)
        )
      )
      var op6 = new Task(
        "ID 06-EPCI for OLB/OOL",
        new SimpleTimePeriod( 
          date(3, Calendar.APRIL, 2017),
          date(4, Calendar.MARCH, 2019)
        )
      )
      var op7 = new Task(
        "ID 07-Test/Comm OOL ",
        new SimpleTimePeriod( 
          date(4, Calendar.MARCH, 2019),
          date(6, Calendar.MARCH, 2019)
        )
      )
      var op8 = new Task(
        "ID 08-Certify OOL",
        new SimpleTimePeriod( 
          date(3, Calendar.APRIL, 2017),
          date(6, Calendar.MARCH, 2019)
        )
      )
      var op9 = new Task(
        "ID 09-Marine Warranty Services",
        new SimpleTimePeriod( 
          date(19, Calendar.AUGUST, 2018),
          date(6, Calendar.MARCH, 2019)
        )
      )       
      bpptaskseries.add(op1)
      bpptaskseries.add(op2)
      bpptaskseries.add(op3)
      bpptaskseries.add(op4)
      bpptaskseries.add(op5)
      bpptaskseries.add(op6)
      bpptaskseries.add(op7)
      bpptaskseries.add(op8)
      bpptaskseries.add(op9)

      //bpptaskseries.add(op2)
      var collectiont = new TaskSeriesCollection()
      collectiont.add(bpptaskseries)

      collectiont
    }
    def createChart(dataset : IntervalCategoryDataset):JFreeChart =
      ChartFactory.createGanttChart(
      "",  // chart title
      "",  // domain axis label
      "",  // range axis label
      dataset,  // data
      false,  // include legend
      true,  // tooltips
      false  // urls
      )
   
  }
}
