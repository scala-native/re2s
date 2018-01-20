package re2s

import org.scalatest.FunSuite
import TestUtils._


class NamedGroupSuite() extends FunSuite {
  ignore("(Not supported) named group (re2 syntax)") {
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

  ignore("(Not supported) named group (java syntax)") { // 620
    val m = matcher(
      "from (?<S>.*) to (?<D>.*)",
      "from Montreal, Canada to Lausanne, Switzerland"
    )
    import m._

    assert(find())
    assert(group("S") == "Montreal, Canada")
    assert(group("D") == "Lausanne, Switzerland")
  }

  ignore("(Not Supported) start(name)/end(name) re2 syntax") {
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

  ignore("(Not Supported) start(name)/end(name) java syntax") { // 620
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
  }

  ignore(
    "(Not Supported) appendReplacement/appendTail with group replacement by name") {
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
    assert(
      buf.toString ==
        "such Montreal, Canada, wow Lausanne, Switzerland")
  }
}