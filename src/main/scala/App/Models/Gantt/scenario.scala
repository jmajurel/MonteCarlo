package com.montecarlo

import org.apache.poi._
import java.io.FileInputStream
import org.apache.poi.xssf.usermodel.XSSFWorkbook


class Scenario(id: Int, name:String, path:String){

  def load(){
    var file = new FileInputStream("inputfile.xlsx")
    var wb = new XSSFWorkbook(file)
    var sheet = wb.getSheetAt(0)
    var nbrow = sheet.getPhysicalNumberOfRows()
    println("nbrow in sheet:"+nbrow)
    var row = sheet.getRow(21)
    var cellop = row.getCell(13)
    var cellpreop = row.getCell(14)
    var cellduration = row.getCell(15)
    var opname = cellop.getStringCellValue()
    var oppreop = cellpreop.getStringCellValue()
    var opduration = cellduration.getStringCellValue()
    //var op1 = Operation(id=1 , name:String, statedate:Date, duration:Int, cost:Double)
    
    println("opname:"+opname)
    println("oppreop:"+oppreop)
    println("opduration:"+opduration)
  }
  def store() = println("store")
}
