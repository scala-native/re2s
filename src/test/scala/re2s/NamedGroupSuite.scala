package re2s

import org.scalatest.FunSuite
import TestUtils._


class NamedGroupSuite() extends FunSuite {
  test("named group (java syntax)") {
    val m = matcher(
      "from (?<S>.*) to (?<D>.*)",
      "from Montreal, Canada to Lausanne, Switzerland"
    )
    import m._

    assert(find())
    assert(group("S") == "Montreal, Canada")
    assert(group("D") == "Lausanne, Switzerland")
  }

  test("start(name)/end(name) java syntax") {
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

  test(
    "appendReplacement/appendTail with group replacement by name") {
    val buf = new StringBuffer()
    val m = matcher(
      "from (?<S>.*) to (?<D>.*)",
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