package com.montecarlo

import org.apache.poi._
import java.io.File
import java.io.FileNotFoundException
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.CellType

trait Models extends GanttModel{ this: MVC =>

  object InputFileKeyWord{
    operation: String = "<op>"
    preop: String = "<pre_op>"
    startdate: String = "<start_date>"
    bcdurationcustomer: String = "<day_c">
  }

  class Model{

    /*case class Operation(
      name: String,
      predecessor: Operation,
      startdate: Date,
      bcdurationcustomer: Int,
      bcdurationbpp: Int,
      bconeofcostcustomer: Double,
      bcdayratecostcustomer: Double,
      bconeofcostbpp: Double,
      bcdayratecostbpp: Double,
      pdffunctioncost: String,
      pdffunctionduration: String
    )*/

    case class Operation(name: String)

    var gantmodel = new GanttModel()

    /**
     * load the excel input file containing the data related to a scenario
     */
    def load(){

      try{
        val pkg = OPCPackage.open(new File("Scenario_Operations_Overview_DEV.xlsx"))
        val wb = new XSSFWorkbook(pkg)
        val sheet = wb.getSheetAt(0)
        var row: Int = 0

        //for(row <- 5 until sheet.getPhysicalNumberOfRows()){
        for(row <- 5 to 6){

          var currentrow = sheet.getRow(row)
          var column: Int = 0

          for(column <- 1 until currentrow.getLastCellNum()){

            var currentcell = currentrow.getCell(column)
            println("getCellTypeEnum():"+currentcell.getCellTypeEnum())

            currentcell.getCellTypeEnum() match {
              case CellType.STRING =>
                println("New Operation object loaded:"+Operation(currentcell.getStringCellValue()))
              case _ =>
                println("data not a string")
            }
          }
        }
        pkg.close()
      }
      catch {
        case ex: FileNotFoundException => {
          println("Missing file exception")
        }
      }
        /*println("nbrow in sheet:"+nbrow)
        var row = sheet.getRow(5)
        var cellop = row.getCell(13)
        var cellpreop = row.getCell(14)
        var cellduration = row.getCell(15)
        var opname = cellop.getStringCellValue()
        var oppreop = cellpreop.getStringCellValue()
        // var opduration = cellduration.getStringCellValue()
        //var op1 = Operation(id=1 , name:String, statedate:Date, duration:Int, cost:Double)

        println("opname:"+opname)
        println("oppreop:"+oppreop)
        //println("opduration:"+opduration)
        */
        }
  }
}
