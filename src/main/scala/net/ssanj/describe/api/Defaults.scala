package net.ssanj.describe.api

import scala.math.Ordering

class Defaults[T: Show : Ordering](values: Seq[T])(
  implicit toFormat: Seq[T] => Format[T], stringtToPrint: String => Print) {

  def d1() = values.sorted.num.print

  def d2() = values.sorted.nl.print
}

class Default[T: Show](value: T)(implicit stringtToPrint: String => Print) {
  def d1() = implicitly[Show[T]].show(value).print
}