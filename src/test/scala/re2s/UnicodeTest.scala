package re2s

import org.scalatest.FunSuite

class UnicodeTest() extends FunSuite {
  test("FoldConstants") {
    var last = -1
    var i    = 0
    while (i <= Unicode.MAX_RUNE) {
      var continue = false
      if (Unicode.simpleFold(i) == i) continue = true
      if (!continue) {
        if (last == -1 && Unicode.MIN_FOLD != i)
          fail("MIN_FOLD=%#U should be %#U".format(Unicode.MIN_FOLD, i))
        last = i
      }
      i += 1
    }
    if (Unicode.MAX_FOLD != last)
      fail("MAX_FOLD=%#U should be %#U".format(Unicode.MAX_FOLD, last))
  }
}
