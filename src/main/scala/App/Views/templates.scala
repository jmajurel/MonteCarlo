package com.montecarlo

import scalafx.Includes._
import scalafx.scene.control.{TextField,Button,TabPane}
import scalafx.scene.text.{Font, FontWeight, Text}
//import javafx.scene.layout.{Border,BorderStroke,CornerRadii,BorderWidths,BorderStrokeStyle}
import scalafx.scene.layout.BorderPane
import scalafx.scene.paint.Color
import scalafx.beans.property.ObjectProperty

object Templates{
  val titlefont =  Font.font(null, FontWeight.Bold, 20)
  val coretextfont = Font.font(null, FontWeight.Normal, 15)
}

class TempTextFieldIndic(txt:String) extends TextField{
  editable = false
  text=txt
  maxWidth = 100
}
class TempTextFieldCtr(txt:String) extends TextField{
  editable = true
  text = txt
  maxWidth = 100
}

class TempButton(txt:String) extends Button{

  minWidth = 100
  maxWidth = 100
  minHeight = 45
  maxHeight = 45
  text = txt
  font = Font.font(null, FontWeight.Bold, 15)
}

class TempTabPane extends TabPane  
class TempBorderPane extends BorderPane

/*class TempTabPane extends TabPane { 
  val stroke = new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
  border = new Border(stroke)
}


class TempBorderPane extends BorderPane {
  val stroke = new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
  border = new Border(stroke)
}*/
