package com.montecarlo

import java.io.File
import org.scalatest._
//import org.scalatest.junit.AssertionsForJUnit
import org.junit.{Before, After}
import org.testfx.api.FxToolkit
import org.testfx.framework.junit.ApplicationTest

//import scalafx.application.JFXApp

abstract class AcceptanceSpec extends FeatureSpec with GivenWhenThen with Matchers 
abstract class TestAppFX extends ApplicationTest {

  @Before
  def setUpClass(): Unit={
    ApplicationTest.launch(App.class)
  }
  @Override
  def start(stage: Stage): Unit={
    stage.show
  }

  @After
  def atferEachTest(): Unit = {
    FxToolkit.hideStage
    release(new KeyCode[]{})
    release(new MouseButton[]{})
  }

}
/*class AcceptanceTesting extends AcceptanceSpec {

  info("As a User")
  info("I want to obtain an output file for each scenario containing cost/time estimations")
  info("I can refer to it for final choose")
  feature("Outputs") {
    scenario("User wants output file") {
      Given("An input file representing a scenario")
        val inputfile = "2a_SING_Scala"
      When("I launch the simulator")
        //model.runMonteCarlo(scenario=inputfile, numberofrun=10000) 
      Then("At the end of the simulation, A outfile is generated containg results")
        new File(inputfile +"_out.xlsx") should exist
    }
  }

  info("As a User")
  info("I want to get the correct total time estimation")
  info("I can add it in the report")
  feature("Estimation") {
    scenario("User wants correct result") {
      Given("A reference input file")
        val inputfile =

      When("I launch the simulator")

      Then("The total time estimation is correct")
    }
  }
}*/

class AcceptanceTesting extends TestAppFX {

  @Test
  def clickOnRun(): Unit={
    clickOn("#Run")
  }
}
