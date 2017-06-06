package view.viewer
  /** This is the package level documentation for the package 
    * `org.my.parent.child`. Note that this documentation
    * uses Wiki markup rather than plain vanilla HTML. This
    * makes it:
    *
    * - Easier to write.
    * - Easier to read by a maintainer looking at the code
    *   rather than the docs in a web browser.
    * - More likely to work when written to some other format.
    *
    * This Scaladoc comment documents both the package, and 
    * the "package object".
    */ 
import java.util.Date
import java.util.Calendar
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


object Gantt{

	var gantt = new Gantt
	var dataset = gantt.createDataset
	var ganttchart = gantt.createChart(dataset)
	val viewerdimension = new Dimension(550, 300)
	
  gantt.configAxis(ganttchart)
	gantt.configGanttStyle(ganttchart)

	var viewer = new ChartPanel(ganttchart) 
	viewer.setPreferredSize(viewerdimension)
	
}

class Gantt{

  val lightblue = new Color(0, 176, 240)
  var taskscollection = new TaskSeriesCollection()
  
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
  	axis.setLabelPaint(Color.lightGray)
  	axis.setVerticalTickLabels(true)
  	axis.setTickLabelFont(new Font(null, Font.PLAIN, 10))
	  
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

  	plot.getRenderer().asInstanceOf[CategoryItemRenderer].setBaseItemLabelGenerator( new StandardCategoryItemLabelGenerator("{1}",NumberFormat.getPercentInstance()) {

      override def  generateRowLabel( dataset: CategoryDataset, row: Int) :String = {
          return "Your Row Text  " + row;
      }
  
  
      override def generateColumnLabel( dataset:CategoryDataset, column: Int) : String = {
          return "Your Column Text  " + column;
      }
  
      
      override def generateLabel( dataset:CategoryDataset, row: Int, column: Int) :String = {
        var percentage = dataset.asInstanceOf[TaskSeriesCollection].getPercentComplete(row,column).asInstanceOf[Double]*100
        
        return percentage.toString + "%"
      }


    })    
  	//plot.getRenderer().asInstanceOf[CategoryItemRenderer].setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator())
  	plot.getRenderer().asInstanceOf[CategoryItemRenderer].setBaseItemLabelsVisible(true)
	  plot.getRenderer().asInstanceOf[CategoryItemRenderer].setBaseItemLabelPaint(Color.BLACK)
	  plot.getRenderer().asInstanceOf[CategoryItemRenderer].setBasePositiveItemLabelPosition(new ItemLabelPosition(
	          ItemLabelAnchor.INSIDE6, 
	          TextAnchor.BOTTOM_CENTER)
	  )
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
	
		val s1 = new TaskSeries("Scheduled")

		var t1 = new Task("OP1",
				   new SimpleTimePeriod( date(1, Calendar.MAY, 2017),
										 date(5, Calendar.MAY, 2017)))
		t1.setPercentComplete(1)
		s1.add(t1)

		s1.add(new Task("OP2",
				   new SimpleTimePeriod( date(5, Calendar.MAY, 2017),
										 date(8, Calendar.MAY, 2017))))

		s1.add(new Task("OP4",
				   new SimpleTimePeriod( date(8, Calendar.MAY, 2017),
										 date(15, Calendar.MAY, 2017))))
		s1.add(new Task("OP4",
				   new SimpleTimePeriod( date(15, Calendar.MAY, 2017),
										 date(29, Calendar.MAY, 2017))))
										 
		val s2 = new TaskSeries("Actual")
		
		s2.add(new Task("OP3",
				   new SimpleTimePeriod( date(8, Calendar.MAY, 2017),
										 date(20, Calendar.MAY, 2017))))
		var collectiont = new TaskSeriesCollection()
		collectiont.add(s1)
		collectiont.add(s2)
		collectiont
	}
	
	def createChart(dataset : IntervalCategoryDataset):JFreeChart = {
		
		var chart = ChartFactory.createGanttChart(
			"",  // chart title
			"",  // domain axis label
			"",  // range axis label
			dataset,  // data
			false,  // include legend
			true,  // tooltips
			false  // urls
		)
		chart
	}

}