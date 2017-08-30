package com.montecarlo

import scala.collection.mutable.{Map => MMap}
import org.apache.poi.ss.usermodel.{WorkbookFactory, DataFormatter}
import org.apache.poi.ss.usermodel.{Row, Cell, CellType}
import org.apache.poi.ss.usermodel.DataFormat
import org.apache.poi.xssf.usermodel.{XSSFWorkbook, XSSFSheet}
import java.io.{File, FileInputStream, FileOutputStream}

trait FileManager {this: Database =>
  
  class FileManager {

    /**
     * load the input file containing the data related to a scenario
     */
    def read(filename: String): Map[String, Operation]={

      /* useful regex which analyse the text */
      val regexop = raw"([a-zA-Z]+\d+(\-\d+)?)".r 
      val regexpre = raw"([a-zA-Z]+\d+(?:\-\d+)?)".r
      val regexpdfarg = raw"(\d+(?:\.\d+)?),(\d+(?:\.\d+)?)".r
      
      /**
       * This map contains the column reference of each items contain in the input file.
       * Unfortunately, the autodetection of items in the input file is not yet implemented, the follo    wing column numbers are hardcoded in the following map.
       */
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
        "<pdf_type_duration>" -> 17,
        "<pdf_parameters_durations>" -> 18,
        "<pdf_type_cost>" -> 19,
        "<pdf_parameters_cost>" -> 20
       )

      /**
      * create root task instance
      */
      val root = Operation (
        name = "root",
        predecessor = List[String](),
        startdate=None,
        bcdurext = 0,
        bcdurbpp = 0,
        bconeoffcostext = None,
        bcdayratext = None,
        bconeoffcostbpp = None,
        bcdayratebpp = None,
        pdffuncdur = "",
        pdfdurargs = Vector[Double](),
        pdffunccost = "",
        pdfcostargs = Vector[Double]()
      )

      val workbook = WorkbookFactory.create(new File(filename + ".xlsx"))
      val sheet = workbook.getSheetAt(0)
      var endfile = false
      var row: Int = 1
      
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

          if (cname != null) cname.getCellTypeEnum match {
            case CellType.STRING => cname.getStringCellValue match {
              case regexop(res) => name = res
              case "<END>" => endfile = true
              case _ => println(f"<op> value at row: ${row} is unreadable")
            }
            case _ => println(f"Type <op> at row: ${row} is not matching")
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
    def write(filename: String, resultsmc: Results) {

      val myworkbook = new XSSFWorkbook
      val sheet = myworkbook.createSheet("Summary")
 
      def createSummaryTable(sheet: XSSFSheet, results: Results) {
        val sumtable = sheet.createTable
        sumtable.setName("Summaryofresult")
        sumtable.setDisplayName("Summaryofresult")
      }

     
     
      val fileoutstream = new FileOutputStream(filename + "_out.xlsx")
      myworkbook.write(fileoutstream)
      fileoutstream.close
    }
  }
}
