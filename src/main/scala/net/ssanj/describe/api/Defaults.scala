package net.ssanj.describe.api

import scala.math.Ordering

class Defaults[T: Show : Ordering](values: Seq[T])(implicit toFormat: Seq[T] => Format[T], stringtToPrint: String => Print) {
  val defaults: Unit = stringtToPrint(toFormat(values.sorted).num).print
}