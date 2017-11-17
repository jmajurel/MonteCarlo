package com.montecarlo.testing

import com.montecarlo.model.FileManager
import com.montecarlo.model.BusinessModel
import com.montecarlo.model.Operation
import com.montecarlo.model.PDFunctions

import org.scalatest._
import breeze.stats.distributions.Uniform
import java.io.File

abstract class UnitSpec extends FlatSpec with Matchers
class UnitTesting extends FileManagerUT with BusinessModelUT with PDFunctionsUT 

trait FileManagerUT extends UnitSpec with FileManager {

  private val referencefile001 = "Test_21-10-17"
  private val filemanager = new FileManager
  private var modulename: String = "Module: [filemanager]"

  ("Function: [read], " + modulename) should "return values" in {
    assume((new File(referencefile001)).exists)
    assert(filemanager.read(referencefile001).nonEmpty)
  }

  ("Function: [write], " + modulename) should "generate an output file" in {
    val file = new File(referencefile001 + "_out.xlsx")
    if(file.exists)
      file.delete

    filemanager.write(referencefile001,List(10000000.0, 20000000.0, 30000000.0),List(100.0,200.0,300.0))
    new File(referencefile001+"_out.xlsx") should exist
  }
}

trait BusinessModelUT extends UnitSpec with BusinessModel {

  private val businessmodel = new BusinessModel
  private val modulename = "Module: [businessmodel]"

  ("Function: [convertDaysToSec], " + modulename) should "return correct result" in {
    assertResult(86400){
      businessmodel.convertDaysToSec(1.0)
    }
  }

  case class PdfFunction(
    val arg1: Double,
    val arg2: Double
  )
  val normal = PdfFunction(arg1=1.0, arg2=0.0000001)
  val halfnormal = PdfFunction(arg1=1.0, arg2=0.101326138651962)
  val invhalfnormal = PdfFunction(arg1=1.0, arg2=0.060795683)
  val extreme = PdfFunction(arg1=1.003385, arg2=0.072920)

  val pdfs = Map(
    "normal" -> normal, 
    "half_normal" -> halfnormal, 
    "inv_half_normal" -> invhalfnormal, 
    "extreme" -> extreme
  ) 

  ("Function: [MCPdfs(normal)], " + modulename) should "return a value greater than 0" in {
    val res = businessmodel.mcPDFs("normal", pdfs("normal").arg1, pdfs("normal").arg2)
    res should be >(0.0) 
  }

  ("Function: [MCPdfs(halfnormal)], " + modulename) should "return a value greater than the mode value" in {
    val res = businessmodel.mcPDFs("half_normal", pdfs("half_normal").arg1, pdfs("half_normal").arg2)
    res should (be >(0.0) and be >(pdfs("half_normal").arg2))
  }

  ("Function: [MCPdfs(extreme)], " + modulename) should "return a value greater than 0" in {
    val res = businessmodel.mcPDFs("extreme", pdfs("extreme").arg1, pdfs("extreme").arg2)
    res should be >(0.0) 
  }

  /* Extracted from the Test_25-10-17.xlsx*/
  val opfortesting = Operation (
    name = "FakeOp",
    predecessor = List[String](),
    predefstartdate = None,
    bcdurext = 18,
    bcdurbpp = 12,
    bconeoffcostext = None,
    bcdayratext = Some(337250),
    bconeoffcostbpp = None,
    bcdayratebpp = Some(194750),
    pdffuncdur = "half_normal",
    pdfdurargs = Vector(halfnormal.arg1, halfnormal.arg2),
    pdffunccost = "inv_half_normal",
    pdfcostargs = Vector(invhalfnormal.arg1, invhalfnormal.arg2)
  )

  ("Function: [mcDurationCalc (with op duration half_normal)], " + modulename) should "return a value greater than the reference base case duration" in {
    val resduration = businessmodel.mcDurationCalc(opfortesting)
    resduration should be > (opfortesting.bcdurbpp).toDouble 
  }

  ("Function: [mcCostCalc (with op cost inv_half_normal)], " + modulename) should "return a value less than the reference base case cost" in {
    val rescost = businessmodel.mcCostCalc(node=opfortesting, duration=Some(opfortesting.bcdurbpp))
    rescost should be < (opfortesting.bcdayratebpp.get * opfortesting.bcdurbpp)  
  }
}

trait PDFunctionsUT extends UnitSpec with PDFunctions {

  private val modulename = "Module: [PDFunctions]"

  ("Class: [BPPareto], " + modulename) should "returns value greater than 1" in {
    val respareto = BPPareto(1.0, 14.531303).inverseCdf(Uniform(0,1).sample)
    respareto should be > 1.0
  }

  ("Class: [BPPExtreme], " + modulename) should "returns value than 0" in {
    val resextreme = BPPExtreme(1.003385, 0.072920).inverseCdf(Uniform(0,1).sample)
    resextreme should be > 0.0
  }
}


