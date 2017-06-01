package ui.viewer

import java.util.Date
import java.util.Calendar
import java.text.SimpleDateFormat
import java.awt.Dimension
import java.awt.Color

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
import java.awt.Paint

object Ganttpanel{

	var panel = new Ganttpanel()
	var dataset = panel.createDataset()
	//dataset.setBackground(Color.white)
	var gantt_chart = panel.createChart(dataset)
			
	var plot = new CategoryPlot()
	var range = new DateAxis()
	
	plot= gantt_chart.getPlot().asInstanceOf[CategoryPlot]
	range = plot.getRangeAxis().asInstanceOf[DateAxis]
	range.setDateFormatOverride(new SimpleDateFormat("EEE MM/dd/yyyy"))
	//gantt_chart.setBorderVisible(false)
	//br.setItemMargin(.01)
	//setCategoryMargin(0)

	//val lightblue = new Color(0, 176, 240)
	
	//plot.getRenderer().setBasePaint(lightblue)
	plot.setBackgroundPaint(Color.white)
	plot.setRangeGridlinePaint(Color.black)
	plot.setDomainGridlinePaint(Color.black)
	var gantt = new ChartPanel(gantt_chart)
	gantt.setPreferredSize(new Dimension(550, 300))
	
}
class Ganttpanel (){

    def date( day : Int,  month: Int, year: Int):Date ={

        var calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        var result = calendar.getTime()
        result

    }
	def createDataset():TaskSeriesCollection = {
	
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
				"",              // domain axis label
				"",              // range axis label
				dataset,             // data
				true,                // include legend
				true,                // tooltips
				false                // urls
			)
			
			chart
	}
	

}