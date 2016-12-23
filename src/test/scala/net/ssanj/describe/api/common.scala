package net.ssanj.describe
package api

trait Show[C, T] {
  def show(value: T): String
}

object Show {
  def apply[C, T](implicit showct: Show[C, T]): Show[C, T] = showct

  def create[C, T](print: T => String): Show[C, T] = new Show[C, T] {
    def show(value: T): String = print(value)
  }

  implicit def fromSeq[C, T](implicit showct: Show[C, T]): Show[C, Seq[T]] = {
    Show.create[C, Seq[T]](_.map(v => s"\t${showct.show(v)}").mkString("\n"))
  }

  sealed trait AnyContext

  implicit val booleanShow = Show.create[AnyContext, Boolean](_.toString)
}