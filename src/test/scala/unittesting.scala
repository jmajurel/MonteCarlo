package com.montecarlo
import collection.mutable.Stack
import org.scalatest._

abstract class UnitSpec extends FlatSpec with Matchers


class UnitTest01 extends UnitSpec {
  "A stack" should "pop values in last-in-first-outer order" in {
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    assert(stack.pop() ===2)
    assert(stack.pop() ===1)
  }
}
