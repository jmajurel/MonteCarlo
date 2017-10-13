package com.montecarlo

import scala.collection.mutable.{Map => MMap}
import org.apache.poi.ss.usermodel.{WorkbookFactory, DataFormatter, DataFormat, Row, Cell, CellType}
import org.apache.poi.ss.util.{AreaReference, CellReference}
import org.apache.poi.xssf.usermodel.{XSSFWorkbook, XSSFSheet, XSSFCell, XSSFRow, XSSFTable}
import java.io.{File, FileInputStream, FileOutputStream}
import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._
import java.time._

trait FileManager {this: Database =>
  
  class FileManager {

    /**
     * load the input file containing the data related to a scenario
     */
    def read(filename: String): Map[String, Operation]={

      /* useful regex which analyse the text */
      val regexop = raw"([a-zA-Z]+\d+(?:\-\d+)?)".r 
      val regexpdfarg = raw"(\d+(?:\.\d+)?),(\d+(?:\.\d+)?)".r
      
      /**
       * This map contains the column reference of each items contain in the input file.
       * Unfortunately, the autodetection of items in the input file is not yet implemented, the follo    wing column numbers are hardcoded in the following map.
       */
      /*val columnrefitems = Map(
        "<op>" -> 6,
        "<pre_op>" -> 7,
        "<start_date>" -> 8,
        "<day_c>" -> 9,
        "<day_b>" -> 10,
        "<one_off_c>" -> 11,
        "<day_rate_c>" -> 12,
        "<one_off_b>" -> 13,
        "<day_rate_b>" -> 14,
        "<pdf_type_duration>" -> 17,
        "<pdf_parameters_durations>" -> 18,
        "<pdf_type_cost>" -> 19,
        "<pdf_parameters_cost>" -> 20
       )*/ 
       val columnrefitems = Map(
         "<op>" -> 6,
         "<pre_op>" -> 7,
         "<start_date>" -> 8,
         "<day_c>" -> 9,
         "<day_b>" -> 10,
         "<one_off_c>" -> 11,
         "<day_rate_c>" -> 12,
         "<one_off_b>" -> 13,
         "<day_rate_b>" -> 14,
         "<pdf_type_duration>" -> 15,
         "<pdf_parameters_duration>" -> 16,
         "<pdf_type_cost>" -> 17,
         "<pdf_parameters_cost>" -> 18
       )

      val workbook = WorkbookFactory.create(new File(filename + ".xlsx"))
      val sheet = workbook.getSheetAt(0)
      var endfile = false
      var row: Int = 1
      //var row: Int = 1

      var opmap: MMap[String,Operation] = MMap("root" -> root)

      while (row < sheet.getPhysicalNumberOfRows & endfile!=true) {

        var currentrow = sheet.getRow(row)

        if (currentrow != null) {

          var name: String = ""
          var predecessor = List[String]() 
          var predefstartdate : Option[LocalDate] = None
          var bcdurext:Double = 0
          var bcdurbpp: Double = 0
          var bconeoffcostext: Option[Double] = None
          var bcdayratext: Option[Double] = None
          var bconeoffcostbpp: Option[Double] = None
          var bcdayratebpp: Option[Double] = None
          var pdffuncdur: String = ""
          var pdfdurargs = Vector[Double]()
          var pdffunccost: String = ""
          var pdfcostargs = Vector[Double]()

          val cname = currentrow.getCell(columnrefitems("<op>"))
          val cpredecessor = currentrow.getCell(columnrefitems("<pre_op>"))
          val cpredefstartdate = currentrow.getCell(columnrefitems("<start_date>"))
          val cbcdurext = currentrow.getCell(columnrefitems("<day_c>"))
          val cbcdurbpp = currentrow.getCell(columnrefitems("<day_b>"))
          val cbconeoffcostext = currentrow.getCell(columnrefitems("<one_off_c>"))
          val cbcdayratext = currentrow.getCell(columnrefitems("<day_rate_c>"))
          val cbconeoffcostbpp = currentrow.getCell(columnrefitems("<one_off_b>"))
          val cbcdayratebpp = currentrow.getCell(columnrefitems("<day_rate_b>"))
          val cpdffuncdur = currentrow.getCell(columnrefitems("<pdf_type_duration>"))
          val cpdfdurargs = currentrow.getCell(columnrefitems("<pdf_parameters_duration>"))
          val cpdffunccost = currentrow.getCell(columnrefitems("<pdf_type_cost>"))
          val cpdfcostargs = currentrow.getCell(columnrefitems("<pdf_parameters_cost>"))

          if (cname != null) {
            cname.getCellTypeEnum match {
              case CellType.STRING => {
                if(cname.getStringCellValue == "<END>") 
                {
                  println(f"End of file detected at row: [${row}]")
                  endfile = true
                }
                else name = regexop.findFirstIn(cname.getStringCellValue).getOrElse("")
              }
              case CellType.BLANK =>
                println(f"Blank [<op>] at row: [${row}]")
              case _ => 
                println(f"Type [<op>] at row: [${row}] is not matching")
            }
          }
          else println(f"Cell: [<op>] at row: [${row}], column: [${columnrefitems("<op>")}] doesn't exist")

          if(cpredecessor != null) {
            cpredecessor.getCellTypeEnum match {
              case CellType.STRING => {
                var predecessors = cpredecessor.getStringCellValue
                if(predecessors == "<END>"){
                  println(f"End of file detected at row: [${row}]")
                  endfile = true
                }
                else predecessor = regexop.findAllIn(predecessors).toList
              }
              case CellType.BLANK =>
                println(f"Blank [<pre_op>] at row: [${row}]")
              case _ => 
                println(f"Type [<pre_op>] at row: [${row}] is not matching")
            }
          }
          else println(f"Cell: [<pre_op>] at row: [${row}], column: [${columnrefitems("<pre_op>")}] doesn't exist")

          if(cpredefstartdate != null)
            cpredefstartdate.getCellTypeEnum match {
              case CellType.STRING =>
                if(cpredefstartdate.getStringCellValue == "<END>"){
                  println(f"End of file detected at row: [${row}]")
                  endfile = true
                }
              case CellType.NUMERIC => {
                val date = cpredefstartdate.getDateCellValue
                predefstartdate = Some(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
              }
              case CellType.BLANK =>
                println(f"Blank [<start_date>] at row: [${row}]")
              case _ =>
                println(f"Unreadable [<start_date>] at row: [${row}]")
            }
          else println(f"Cell: [<start_date>] at row: [${row}], column: [${columnrefitems("<start_date>")}] doesn't exist")

          if(cbcdurext != null)
            cbcdurext.getCellTypeEnum match {
              case CellType.NUMERIC => 
                bcdurext = cbcdurext.getNumericCellValue
              case CellType.STRING => 
                if(cbcdurext.getStringCellValue == "<END>"){
                  println(f"End of file detected at row: [${row}]")
                  endfile = true
                }
              case CellType.BLANK =>
                println(f"Blank [<day_ext>] at row: [${row}]")
              case _ => 
                println(f"Unreadable [<day_ext>] at row: [${row}]")
            }
          else println(f"Cell: [<day_ext>] at row: [${row}], column: [${columnrefitems("<day_c>")}] doesn't exist")

          if(cbcdurbpp != null)
            cbcdurbpp.getCellTypeEnum match {
              case CellType.NUMERIC => 
                bcdurbpp = cbcdurbpp.getNumericCellValue
              case CellType.STRING => 
                if(cbcdurbpp.getStringCellValue == "<END>"){
                  println(f"End of file detected at row: [${row}]")
                  endfile = true
                }
              case CellType.BLANK =>
                println(f"Blank [<day_b>] at row: [${row}]")
              case _ => 
                println(f"Unreadable [<day_b>] at row: [${row}]")
            }
          else println(f"Cell: [<day_b>] at row: [${row}], column: [${columnrefitems("<day_b>")}] doesn't exist")

          if(cbconeoffcostext != null)
            cbconeoffcostext.getCellTypeEnum match { 
              case CellType.NUMERIC => 
                bconeoffcostext = Some(cbconeoffcostext.getNumericCellValue)
              case CellType.STRING => 
                if (cbconeoffcostext.getStringCellValue == "<END>"){
                  println(f"End of file detected at row: [${row}]")
                  endfile = true
                }
              case CellType.BLANK =>
                println(f"Blank [<one_off_ext>] at row: [${row}]")
              case _ => 
                println(f"Unreadable [<one_off_ext>] at row: [${row}]")
            }
          else println(f"Cell: [<one_off_ext>] at row: [${row}], column: [${columnrefitems("<one_off_c>")}] doesn't exist")

          if(cbcdayratext != null)
            cbcdayratext.getCellTypeEnum match {
              case CellType.NUMERIC => 
                bcdayratext = Some(cbcdayratext.getNumericCellValue)
              case CellType.STRING => 
                if(cbcdayratext.getStringCellValue == "<END>"){
                  println(f"End of file detected at row: [${row}]")
                  endfile = true
                }
              case CellType.BLANK =>
                println(f"Blank [<day_rate_ext>] at row: [${row}]")
              case _ => 
                println(f"Unreadable [<day_rate_ext>] at row: [${row}]")
            }
          else println(f"Cell: [<day_rate_ext>] at row: [${row}], column: [${columnrefitems("<day_rate_c>")}] doesn't exist")

          if(cbconeoffcostbpp != null)
            cbconeoffcostbpp.getCellTypeEnum match {
              case CellType.NUMERIC => 
                bconeoffcostbpp = Some(cbconeoffcostbpp.getNumericCellValue)
              case CellType.STRING => 
                if (cbconeoffcostbpp.getStringCellValue == "<END>"){ 
                  println(f"End of file detected at row: [${row}]")
                  endfile = true
                }
              case CellType.BLANK =>
                println(f"Blank [bconeoffcostbpp] at row: [${row}]")
              case _ => 
                println(f"Unreadable [<one_off_b>] at row: [${row}]")
            }
          else println(f"Cell: [<one_off_b>] at row: [${row}], column: [${columnrefitems("<one_off_b>")}] doesn't exist")

          if(cbcdayratebpp != null)
            cbcdayratebpp.getCellTypeEnum match {
              case CellType.NUMERIC => 
                bcdayratebpp = Some(cbcdayratebpp.getNumericCellValue)
              case CellType.STRING => 
                if (cbcdayratebpp.getStringCellValue == "<END>"){ 
                  println(f"End of file detected at row: [${row}]")
                  endfile = true
                }
              case CellType.BLANK =>
                println(f"Blank [<day_rate_b>] at row: [${row}]")
              case _ => 
                println(f"Unreadable [<day_rate_b>] at row: [${row}]")
            }
          else println(f"Cell: [<day_rate_b>] at row: [${row}], column: [${columnrefitems("<day_rate_b>")}] doesn't exist")

          if(cpdffuncdur != null)
            cpdffuncdur.getCellTypeEnum match {
              case CellType.STRING => {
                cpdffuncdur.getStringCellValue match {
                  case "<END>" => {
                    println(f"End of file detected at row: [${row}]")
                    endfile = true
                  }
                  case func => 
                    pdffuncdur=func.toLowerCase  
                }
              }
              case CellType.BLANK =>
                println(f"Blank [<pdf_type_duration>] at row: [${row}]")
              case _ =>
                println(f"Unreadable [<pdf_type_duration>] at row: [${row}]")
            }
          else println(f"Cell: [<pdf_type_duration>] at row: [${row}], column: [${columnrefitems("<pdf_type_duration>")}] doesn't exist")

          if(cpdfdurargs != null)
            cpdfdurargs.getCellTypeEnum match {
              case CellType.STRING => {
                cpdfdurargs.getStringCellValue match {
                  case "<END>" => { 
                    println(f"End of file detected at row: [${row}]")
                    endfile = true
                  }
                  case regexpdfarg(mean,std) => 
                    pdfdurargs = Vector(mean.toDouble,std.toDouble)
                  case _ => 
                    println(f"Unloadable [<pdf_parameters_duration>] at row: [${row}]")
                }
              }
              case CellType.BLANK =>
                println(f"Blank [<pdf_parameters_duration>] at row: [${row}]")
              case _ =>
                println(f"Unreadable [<pdf_parameters_duration>] at row: [${row}]")
            }
          else println(f"Cell: [<pdf_parameters_duration>] at row: [${row}], column: [${columnrefitems("<pdf_parameters_duration>")}] doesn't exist")

          if(cpdffunccost != null)
            cpdffunccost.getCellTypeEnum match {
              case CellType.STRING => {
                cpdffunccost.getStringCellValue match {
                  case "<END>" => { 
                    println(f"End of file detected at row: [${row}]")
                    endfile = true
                  }
                  case func => 
                    pdffunccost = func.toLowerCase
                }
              }
              case CellType.BLANK =>
                println(f"Blank [<pdf_type_cost>] at row: [${row}]")
              case _ =>
                println(f"Unreadable [<pdf_type_cost>] at row: [${row}]")
            }
          else println(f"Cell: [<pdf_type_cost>] at row: [${row}], column: [${columnrefitems("<pdf_type_cost>")}] doesn't exist")

          if(cpdfcostargs != null)
            cpdfcostargs.getCellTypeEnum match {
              case CellType.STRING => {
                cpdfcostargs.getStringCellValue match {
                  case "<END>" => {
                    println(f"End of file detected at row: [${row}]")
                    endfile = true
                  }
                  case regexpdfarg(mean, std) => 
                    pdfcostargs = Vector(mean.toDouble, std.toDouble) 
                  case _ =>
                    println(f"Unloadable [<pdf_parameters_cost>] at row: [${row}]")
                }
              }
              case CellType.BLANK =>
                println(f"Blank [<pdf_parameters_cost>] at row: [${row}]")
              case _ =>
                println(f"Unreadable [<pdf_parameters_cost>] at row: [${row}]")
            }
          else println(f"Cell: [<pdf_parameters_cost>] at row: [${row}], column: [${columnrefitems("<pdf_parameters_cost>")}] doesn't exist")

          if (endfile == false & name != "") { 

            val newOperation = Operation(
              name,
              predecessor,
              predefstartdate, 
              bcdurext,
              bcdurbpp, 
              bconeoffcostext,
              bcdayratext, 
              bconeoffcostbpp, 
              bcdayratebpp,
              pdffuncdur,
              pdfdurargs, 
              pdffunccost,
              pdfcostargs 
            )
            opmap += (name -> newOperation)
          }
          row += 1
        }
      }
      workbook.close()
      opmap.toMap 
    }

    /**
     * generate outputfile containing the monte carlo simulation results
     */
    def write(filename: String, costs: List[Double], durations: List[Double]) {

      val myworkbook = new XSSFWorkbook
      val shsummary = myworkbook.createSheet("Summary")
      val shresults = myworkbook.createSheet("Results")
      val columname = List("", "Cost (M$)", "Duration (Days)")


      def getTableTemplate(sheet: XSSFSheet, title: String): XSSFTable={
        val table = sheet.createTable
        table.setName(title)
        table.setDisplayName(title)

        //set table style
        table.getCTTable.addNewTableStyleInfo
        table.getCTTable.addNewTableStyleInfo.setName("TableStyleMedium2")
        table
      }

      def printSummary(sheet: XSSFSheet, costs: List[Double], durations: List[Double]) {

        val rowname = List("", "min", "mean", "max")

        for((nrow, i) <- rowname.zipWithIndex) {
          val row = sheet.createRow(i)
          for ((ncolumn, j) <- columname.zipWithIndex) {
            val cell = row.createCell(j)
            (ncolumn, nrow) match {
              case ("Cost (M$)","min") => cell.setCellValue(costs.min/1000000)
              case ("Cost (M$)","mean") => cell.setCellValue(costs.sum/costs.size/1000000)
              case ("Cost (M$)","max") => cell.setCellValue(costs.max/1000000)
              case ("Duration (Days)","min") => cell.setCellValue(durations.min.toInt)
              case ("Duration (Days)","mean") => cell.setCellValue((durations.sum/durations.size).toInt)
              case ("Duration (Days)","max") => cell.setCellValue(durations.max.toInt)
              case ("", _) => cell.setCellValue(nrow) //set row header
              case (_, "") => cell.setCellValue(ncolumn) //set colum header
              case _ => cell.setCellValue("")
            }
          }
        }
      }

      def printResults(sheet: XSSFSheet, costs: List[Double], durations: List[Double]) {

        var i: Int=1
        val headerow = sheet.createRow(0)
        headerow.createCell(0).setCellValue("Run Number")
        headerow.createCell(1).setCellValue("Cost (M$)")
        headerow.createCell(2).setCellValue("Duration (Days)")
        for((cost, dur) <- costs.zip(durations)){
          val row = sheet.createRow(i)
          row.createCell(0).setCellValue(i)
          row.createCell(1).setCellValue(cost/1000000)
          row.createCell(2).setCellValue(dur)
          i += 1
        }
      }

      printSummary(shsummary, costs, durations)
      printResults(shresults, costs, durations)

      val fileoutstream = new FileOutputStream(filename + "_out.xlsx")
      myworkbook.write(fileoutstream)
      fileoutstream.close
    }
  }
}
