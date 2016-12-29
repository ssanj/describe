package net.ssanj.describe.api

final class Format[T](values: Seq[T], S: Show[T]) {
  def nl: String = values.map(S.show(_)).mkString("\n")
  def num(): String = values.zipWithIndex.map { case (v, i) => "%2d. %s".format(i + 1, S.show(v)) }.mkString("\n")
}
