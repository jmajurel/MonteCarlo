package com.montecarlo

import org.apache.poi._
import java.io.FileInputStream
import org.apache.poi.xssf.usermodel.XSSFWorkbook

trait Models extends GanttModel{ this: MVC =>

  class Model{

    case class Operation(
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
    )


    var gantmodel = new GanttModel()

    /**
     * load the excel input file containing the data related to a scenario
     */
    def load(){

      val fis = new FileInputStream("Scenario_Operations_Overview_DEV.xlsx")
      val wb = new XSSFWorkbook(fis)
      val sheet = wb.getSheetAt(0)
      val nbrow = sheet.getPhysicalNumberOfRows()
      for (sheet.rowIterator){
        println("value")

      }
      println("nbrow in sheet:"+nbrow)
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
      fis.close()
    }
  }
}
