package re2s

import scala.reflect.ClassTag

object TestUtils {
  def assertThrowsAnd[T: ClassTag](f: => Unit)(pred: T => Boolean): Unit = {
    val cls = implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]]
    assertThrowsImpl[T](cls, f, pred)
  }

  private def assertThrowsImpl[T](expected: Class[T],
                                  f: => Unit,
                                  pred: T => Boolean): Unit = {
    try {
      f
    } catch {
      case exc: Throwable =>
        if (expected.isInstance(exc) && pred(exc.asInstanceOf[T]))
          return
        else
          throw new Exception("fail")
    }
    throw new Exception("fail")
  }

  def matcher(regex: String, text: String): Matcher =
    Pattern.compile(regex).matcher(text)
}