package net.ssanj.describe
package api

trait Show[T] {
  def show(value: T): String
}

trait LowPriorityShowImplicits {
  implicit def toStringToShow[T]: Show[T] = new Show[T] {
    override def show(value: T): String = value.toString
  }
}


object Show extends LowPriorityShowImplicits {

  def apply[T](implicit S: Show[T]): Show[T] = S

  def create[T](print: T => String): Show[T] = new Show[T] {
    import scala.util.Try
    import scala.util.control.NonFatal

    def show(value: T): String =
     try {
      print(value)
     } catch {
      //try to overcome not fatals, linkage errors and assertion errors.
      //don't catch anything else.
      case ex@(NonFatal(_) | _: LinkageError | _: AssertionError) => alternatives(value, ex)
    }

    def alternatives(value: T, ex: Throwable): String = {
      Try(s"${value.getClass} - [ex.getMessage]") getOrElse ("???")
    }
  }
}