package re2s

import org.scalatest.FunSuite

class PatternSuite() extends FunSuite {
  test("a|b") {
    Pattern.compile("a|b")
  }
}