package com.montecarlo

import scala.collection.mutable.{Map => MMap}
import org.apache.poi.ss.usermodel.{WorkbookFactory, DataFormatter, DataFormat, Row, Cell, CellType}
import org.apache.poi.ss.util.{AreaReference, CellReference}
import org.apache.poi.xssf.usermodel.{XSSFWorkbook, XSSFSheet, XSSFCell, XSSFRow, XSSFTable}
import java.io.{File, FileInputStream, FileOutputStream}
import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._

trait FileManager {this: Database =>
  
  class FileManager {

    /**
     * load the input file containing the data related to a scenario
     */
    def read(filename: String): Map[String, Operation]={

      /* useful regex which analyse the text */
      val regexop = raw"([a-zA-Z]+\d+(?:\-\d+)?)".r 
      val regexpre = raw"([a-zA-Z]+\d+(?:\-\d+)?)".r
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
         "<pdf_parameters_durations>" -> 16,
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
          var startdate: Option[String] = None
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
          val cstartdate = currentrow.getCell(columnrefitems("<start_date>"))
          val cbcdurext = currentrow.getCell(columnrefitems("<day_c>"))
          val cbcdurbpp = currentrow.getCell(columnrefitems("<day_b>"))
          val cbconeoffcostext = currentrow.getCell(columnrefitems("<one_off_c>"))
          val cbcdayratext = currentrow.getCell(columnrefitems("<day_rate_c>"))
          val cbconeoffcostbpp = currentrow.getCell(columnrefitems("<one_off_b>"))
          val cbcdayratebpp = currentrow.getCell(columnrefitems("<day_rate_b>"))
          val cpdffuncdur = currentrow.getCell(columnrefitems("<pdf_type_duration>"))
          val cpdfdurargs = currentrow.getCell(columnrefitems("<pdf_parameters_durations>"))
          val cpdffunccost = currentrow.getCell(columnrefitems("<pdf_type_cost>"))
          val cpdfcostargs = currentrow.getCell(columnrefitems("<pdf_parameters_cost>"))

          if (cname != null) {
            if (cname.getCellTypeEnum == CellType.STRING) {
              if (cname.getStringCellValue == "<END>") 
                endfile = true
              else
                name = regexop.findFirstIn(cname.getStringCellValue).getOrElse("")
            }
            else println(f"Type <op> at row: ${row} is not matching")
          }

          if(cpredecessor != null)
            if(cpredecessor.getCellTypeEnum == CellType.STRING) {
              var predecessors = cpredecessor.getStringCellValue
              if(predecessors == "<END>") endfile = true
              else predecessor = regexpre.findAllIn(predecessors).toList
            }

          if(cstartdate != null)
            if(cstartdate.getCellTypeEnum == CellType.STRING){
               cstartdate.getStringCellValue match {
                 case "<END>" => endfile = true
                 case default => startdate = Some(default)
              }
            }

          if(cbcdurext != null)
            cbcdurext.getCellTypeEnum match {
              case CellType.NUMERIC => bcdurext = cbcdurext.getNumericCellValue
              case CellType.STRING => {
                if(cbcdurext.getStringCellValue == "<END>") 
                  endfile = true
              }
              case _ => println("cannot read 'cbcdurext'")
            }

          if(cbcdurbpp != null)
            cbcdurbpp.getCellTypeEnum match {
              case CellType.NUMERIC => bcdurbpp = cbcdurbpp.getNumericCellValue
              case CellType.STRING => {
                if(cbcdurbpp.getStringCellValue == "<END>") 
                  endfile = true
              }
              case _ => println("cannot read 'cbcdurbpp'")
            }

          if(cbconeoffcostext != null)
            cbconeoffcostext.getCellTypeEnum match { 
              case CellType.NUMERIC => bconeoffcostext = Some(cbconeoffcostext.getNumericCellValue)
              case CellType.STRING => {
                if (cbconeoffcostext.getStringCellValue == "<END>") 
                  endfile = true
              }
              case _ => println("cannot read 'cbconeoffcostext'")
            }

          if(cbcdayratext != null)
            cbcdayratext.getCellTypeEnum match {
              case CellType.NUMERIC => bcdayratext = Some(cbcdayratext.getNumericCellValue)
              case CellType.STRING => {
                if(cbcdayratext.getStringCellValue == "<END>") 
                  endfile = true
              }
              case _ => println("cannot read 'cbcdayratext'")
            }

          if(cbconeoffcostbpp != null)
            cbconeoffcostbpp.getCellTypeEnum match {
              case CellType.NUMERIC => bconeoffcostbpp = Some(cbconeoffcostbpp.getNumericCellValue)
              case CellType.STRING => {
                if (cbconeoffcostbpp.getStringCellValue == "<END>") 
                  endfile = true
              }
              case _ => println("cannot read 'cbconeoffcostbpp'")
            }

          if(cbcdayratebpp != null)
            cbcdayratebpp.getCellTypeEnum match {
              case CellType.NUMERIC => bcdayratebpp = Some(cbcdayratebpp.getNumericCellValue)
              case CellType.STRING => {
                if (cbcdayratebpp.getStringCellValue == "<END>") 
                  endfile = true
              }
              case _ => println("cannot read 'cbcdayratebpp'")
            }

          if(cpdffuncdur != null)
            if(cpdffuncdur.getCellTypeEnum == CellType.STRING){
              cpdffuncdur.getStringCellValue() match {
                case "<END>" => endfile = true
                case func => pdffuncdur=func.toLowerCase  
              }
            }

          if(cpdfdurargs != null)
            if(cpdfdurargs.getCellTypeEnum() == CellType.STRING){
              cpdfdurargs.getStringCellValue() match {
                case "<END>" => endfile = true
                case regexpdfarg(mean,std) => pdfdurargs = Vector(mean.toDouble,std.toDouble)
              }
            }

          if(cpdffunccost != null)
            if(cpdffunccost.getCellTypeEnum() == CellType.STRING){
              cpdffunccost.getStringCellValue() match {
                case "<END>" => endfile = true
                case func => pdffunccost = func.toLowerCase
              }
            }

          if(cpdfcostargs != null)
            if(cpdfcostargs.getCellTypeEnum()==CellType.STRING){
              cpdfcostargs.getStringCellValue() match {
                case "<END>" => endfile = true
                case regexpdfarg(mean, std) => pdfcostargs = Vector(mean.toDouble, std.toDouble) 
              }
            }

          if (endfile == false & name != "") { 

            val newOperation = Operation(
              name,
              predecessor,
              startdate, 
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
    def write(filename: String, data: Graph[Operation, DiEdge]) {

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

      def printSummary(sheet: XSSFSheet, op: Operation) {

        val rowname = List("", "min", "mean", "max")

        for((nrow, i) <- rowname.zipWithIndex) {
          val row = sheet.createRow(i)
          for ((ncolumn, j) <- columname.zipWithIndex) {
            val cell = row.createCell(j)
            (ncolumn, nrow) match {
              case ("Cost (M$)","min") => cell.setCellValue(op.mcrescost.rowresults.min)
              case ("Cost (M$)","mean") => cell.setCellValue(op.mcrescost.rowresults.sum/op.mcrescost.rowresults.size)
              case ("Cost (M$)","max") => cell.setCellValue(op.mcrescost.rowresults.max)
              case ("Duration (Days)","min") => cell.setCellValue(op.mcresdur.rowresults.min.toInt)
              case ("Duration (Days)","mean") => cell.setCellValue((op.mcresdur.rowresults.sum/op.mcresdur.rowresults.size).toInt)
              case ("Duration (Days)","max") => cell.setCellValue(op.mcresdur.rowresults.max.toInt)
              case ("", _) => cell.setCellValue(nrow) //set row header
              case (_, "") => cell.setCellValue(ncolumn) //set colum header
              case _ => cell.setCellValue("")
            }
          }
        }
      }

      def printResults(sheet: XSSFSheet, op: Operation) {

        var i: Int=1
        val headerow = sheet.createRow(0)
        headerow.createCell(0).setCellValue("Run Number")
        headerow.createCell(1).setCellValue("Cost (M$)")
        headerow.createCell(2).setCellValue("Duration (Days)")

        for( (rescost, resdur) <- op.mcrescost.rowresults.zip(op.mcresdur.rowresults)) {
          val row = sheet.createRow(i)
          row.createCell(0).setCellValue(i)
          row.createCell(1).setCellValue(rescost)
          row.createCell(2).setCellValue(resdur)
          i += 1
        }
      }
      printSummary(shsummary, (data get root).toOuter)
      printResults(shresults, (data get root).toOuter)

      val fileoutstream = new FileOutputStream(filename + "_out.xlsx")
      myworkbook.write(fileoutstream)
      fileoutstream.close
    }
  }
}
