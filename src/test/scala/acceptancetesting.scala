package com.montecarlo.testing
import com.montecarlo.mvc.MVC
import com.montecarlo.MonteCarloApp

import java.io.File
import org.scalatest._
/*import org.scalatest.junit.AssertionsForJUnit
import org.junit.Assert._
import org.junit.Test
import org.junit.Before*/

import scalafx.application.JFXApp
import javafx.stage.Stage
import scalafx.scene.Scene

/*import org.testfx.api.FxToolkit
import org.testfx.framework.junit.ApplicationTest
import org.testfx.api.FxAssert.verifyThat
import org.testfx.matcher.base.NodeMatchers.hasText*/

abstract class AcceptanceSpec extends FeatureSpec with GivenWhenThen with Matchers 

/*class AcceptanceTesting extends ApplicationTest {

  override def start(stage: Stage) {
    val mcapp = (new MonteCarloApp)
    mcapp.stage.show()
  }

  @Test 
  def should_contain_button() {
    assertThrows[Any]{
      verifyThat(".button", hasText("click me!")) 
    }
  }
}*/

class AcceptanceTesting extends AcceptanceSpec with BeforeAndAfter {

  val mvc = new MVC
  val referencefile001 = "Test_25-10-17"
  val referencefile002 = "Test_01-11-17"

  after {
    for(referencefile <- List(referencefile001, referencefile002)){
      val file = new File(referencefile + "_out.xlsx")
      if(file.exists)
        file.delete
    }
  }

  info("User Story #11")
  info("As a User")
  info("I want to obtain an output file for each scenario containing cost/time estimations")
  info("I can refer to it for final choose")

  feature("Outputs") {
    scenario("Test ID: #01, User wants output file") {
      Given("An input file representing a scenario")
      When("I launch the simulator")
        mvc.model.runMonteCarlo(scenario=referencefile001 , numberofrun=10000) 
      Then("At the end of the simulation, A outfile is generated containg results")
        new File(referencefile001+"_out.xlsx") should exist
    }
  }

  info("User Story #26")
  info("As a User")
  info("I want to get the correct estimations")
  info("I can add it in the report")

  feature("Estimations") {
    scenario("Test ID: #02, User wants correct cost estimate") {
      Given("A reference input file")
      When("I launch the simulator")
        mvc.model.runMonteCarlo(scenario=referencefile002, numberofrun=100000) 
      Then("The total cost estimation is correct")
        val costs = mvc.model.database.readDBCosts
        val meancost = (costs.sum/costs.size/1000000)
        meancost should be (23.0 +- 0.1)
    }
    scenario("Test ID: #03, User wants correct time estimate") {
      Given("A reference input file")
      When("I launch the simulator")
      Then("The total time estimation is correct")
        val durations = mvc.model.database.readDBDurations
        val meandur = (durations.sum/durations.size).toInt
        meandur should be (295 +- 1)
    }
  }
}
