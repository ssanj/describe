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
    def show(value: T): String = print(value)
  }

  implicit def fromSeq[T](implicit S: Show[T]): Show[Seq[T]] = {
    Show.create[Seq[T]](_.map(v => s"\t${S.show(v)}").mkString("\n"))
  }
}