package com.montecarlo

import org.apache.poi._
import java.io.FileInputStream
import org.apache.poi.xssf.usermodel.XSSFWorkbook


trait Scenario{
  /*var id:Int
  var name: String
  var path: String
  var operations: Array[Operation]  */
  class Scenario{

    
    def load(){
      var file = new FileInputStream("inputfile.xlsx")
      var wb = new XSSFWorkbook(file)
      var sheet = wb.getSheetAt(0)
      var nbrow = sheet.getPhysicalNumberOfRows()
      println("nbrow in sheet:"+nbrow)
      var row = sheet.getRow(7)
      var cell = row.getCell(14)
      var cellvalue = cell.getStringCellValue()
      println("cellvalue:"+cellvalue)
    }
    def store() = println("store")
  }
}
