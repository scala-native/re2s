package re2s

import org.scalatest.FunSuite
import TestUtils._

// Tests are inspired by those projects under Apache2 License:
// j2objc: https://github.com/google/j2objc/blob/master/jre_emul/Tests/java/util/regex/MatcherTest.java#L1
// re2: https://github.com/google/re2/blob/master/re2/testing/re2_test.cc


class MatcherSuite() extends FunSuite {
  test("quoteReplacement") {
    assert(Matcher.quoteReplacement("") == "")
  }

  test("match") {
    val m = matcher("foo", "foobar")
    assert(!m.matches())
  }

  test("replaceAll") {
    assert(matcher("abc", "abcabcabc").replaceAll("z") == "zzz")
  }

  test("replaceFirst") {
    assert(matcher("abc", "abcabcabc").replaceFirst("z") == "zabcabc")
  }

  test("group") {
    val m = matcher("a(\\d)(\\d)z", "_a12z_a34z_")
    import m._

    assert(groupCount == 2)

    assertThrowsAnd[IllegalStateException](group)(
      _.getMessage == "No match found"
    )

    assert(find())
    assert(group == "a12z")
    assert(group(0) == "a12z")
    assert(group(1) == "1")
    assert(group(2) == "2")
    assertThrowsAnd[IndexOutOfBoundsException](group(42))(
      _.getMessage == "No group 42"
    )

    assert(find())
    assert(group == "a34z")
    assert(group(0) == "a34z")
    assert(group(1) == "3")
    assert(group(2) == "4")

    assert(!find())
  }

  test("named group (re2 syntax)") {
    // change pattern to java: "from (?<S>.*) to (?<D>.*)"
    val m = matcher(
      "from (?P<S>.*) to (?P<D>.*)",
      "from Montreal, Canada to Lausanne, Switzerland"
    )
    import m._

    assert(find())
    assert(group("S") == "Montreal, Canada")
    assert(group("D") == "Lausanne, Switzerland")
    assertThrowsAnd[IllegalArgumentException](group("foo"))(
      _.getMessage == "No group with name <foo>"
    )
  }

  test("(Not supported) named group (java syntax)") {
    val m = matcher(
      "from (?<S>.*) to (?<D>.*)",
      "from Montreal, Canada to Lausanne, Switzerland"
    )
    import m._

    assert(find())
    assert(group("S") == "Montreal, Canada")
    assert(group("D") == "Lausanne, Switzerland")
    pending // 620
  }

  test("start(i)/end(i)") {
    val m = matcher("a(\\d)(\\d)z", "012345_a12z_012345")
    import m._

    assertThrowsAnd[IllegalStateException](start)(
      _.getMessage == "No match found"
    )

    assertThrowsAnd[IllegalStateException](end)(
      _.getMessage == "No match found"
    )

    assert(find())

    assert(start == 7)
    assert(end == 11)

    assert(start(0) == 7)
    assert(end(0) == 11)

    assert(start(1) == 8)
    assert(end(1) == 9)

    assert(start(2) == 9)
    assert(end(2) == 10)

    assertThrowsAnd[IndexOutOfBoundsException](start(42))(
      _.getMessage == "No group 42"
    )

    assertThrowsAnd[IndexOutOfBoundsException](end(42))(
      _.getMessage == "No group 42"
    )
  }

  test("start/end") {
    val m = matcher("a(\\d)(\\d)z", "_a12z_a34z_")
    import m._

    assert(find())
    assert(start == 1)
    assert(end == 5)

    assert(find())
    assert(start == 6)
    assert(end == 10)

    assert(!find())
  }

  test("start(name)/end(name) re2 syntax") {
    val m = matcher(
      "from (?P<S>.*) to (?P<D>.*)",
      "from Montreal, Canada to Lausanne, Switzerland"
    )
    import m._

    assert(find())
    assert(start("S") == 5)
    assert(end("S") == 21)

    assert(start("D") == 25)
    assert(end("D") == 46)

    assertThrowsAnd[IllegalArgumentException](start("foo"))(
      _.getMessage == "No group with name <foo>"
    )

    assertThrowsAnd[IllegalArgumentException](end("foo"))(
      _.getMessage == "No group with name <foo>"
    )
  }

  test("(Not Supported) start(name)/end(name) java syntax") {
    // change pattern to java: "from (?<S>.*) to (?<D>.*)"
    val m = matcher(
      "from (?<S>.*) to (?<D>.*)",
      "from Montreal, Canada to Lausanne, Switzerland"
    )
    import m._

    assert(find())
    assert(start("S") == 5)
    assert(end("S") == 21)

    assert(start("D") == 25)
    assert(end("D") == 46)
    pending // 620
  }

  test("appendReplacement/appendTail") {
    val buf = new StringBuffer()

    val m = matcher("a(\\d)(\\d)z", "_a12z_a34z_")
    import m._

    while (find()) {
      appendReplacement(buf, "{" + group + "}")
    }
    appendTail(buf)
    assert(buf.toString == "_{a12z}_{a34z}_")
  }

  test("appendReplacement/appendTail with group replacement by index") {
    val buf = new StringBuffer()
    val m   = matcher("a(\\d)(\\d)z", "_a12z_a34z_")
    import m._
    while (find()) {
      appendReplacement(buf, "{$0}")
    }
    appendTail(buf)
    assert(buf.toString == "_{a12z}_{a34z}_")
  }

  test("appendReplacement/appendTail with group replacement by name") {
    val buf = new StringBuffer()
    val m = matcher(
      "from (?P<S>.*) to (?P<D>.*)",
      "from Montreal, Canada to Lausanne, Switzerland"
    )
    import m._
    while (find()) {
      appendReplacement(buf, "such ${S}, wow ${D}")
    }
    appendTail(buf)
    assert(buf.toString ==
                 "such Montreal, Canada, wow Lausanne, Switzerland")
  }

  test("reset") {
    val m = matcher("a(\\d)(\\d)z", "_a12z_a34z_")
    import m._

    assert(find())
    assert(start == 1)
    assert(end == 5)

    reset()

    assert(find())
    assert(start == 1)
    assert(end == 5)

    assert(find())
    assert(start == 6)
    assert(end == 10)

    assert(!find())
  }

  // we don't support lookahead
  test("(Not supported) hasTransparentBounds/useTransparentBounds") {

    // ?=  <==>  zero-width positive look-ahead
    val m1 = Pattern.compile("foo(?=buzz)").matcher("foobuzz")
    m1.region(0, 3)
    m1.useTransparentBounds(false)
    assert(!m1.matches()) // opaque

    m1.useTransparentBounds(true)
    assert(m1.matches()) // transparent

    // ?!  <==>  zero-width negative look-ahead
    val m2 = Pattern.compile("foo(?!buzz)").matcher("foobuzz")
    m2.region(0, 3)
    m2.useTransparentBounds(false)
    assert(!m2.matches()) // opaque

    m2.useTransparentBounds(true)
    assert(m2.matches()) // transparent
    pending // 620
  }

  test("lookingAt") {
    val m1 = matcher("foo", "foobar")
    assert(m1.lookingAt())
  }

  test("pattern") {
    val p = Pattern.compile("foo")
    assert(
      p.matcher("foobar").pattern() ==
      p
    )
  }

  test("issue #852, StringIndexOutOfBoundsException") {
    val JsonNumberRegex =
      """(-)?((?:[1-9][0-9]*|0))(?:\.([0-9]+))?(?:[eE]([-+]?[0-9]+))?""".r
    val JsonNumberRegex(negative, intStr, decStr, expStr) = "0.000000"
    assert(negative == null)
    assert(intStr == "0")
    assert(decStr == "000000")
    assert(expStr == null)
  }

  private def matcher(regex: String, text: String): Matcher =
    Pattern.compile(regex).matcher(text)
}