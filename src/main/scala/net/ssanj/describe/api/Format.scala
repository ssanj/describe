package net.ssanj.describe.api

trait Show[T] {
  def show(value: T): String
}

class Format[T](values: Seq[T], S: Show[T]) {
  def newline: String = values.map(S.show(_)).mkString("\n")
  def numbered: String = values.zipWithIndex.map { case (v, i) => "%2d. %s".format(i + 1, S.show(v)) }.mkString("\n")
}

class Print(value: String) {
  def print(): Unit = println(value)
}
