package net.ssanj.describe
package api

import scala.math.Ordering

final class Sorted[T](values: Seq[T]) {
  def sortAlpha(implicit ord: Ordering[T]): Seq[T] = values.sorted
  //sort by parents - may need to display parent info as well.
  def sort(isBefore: (T, T) => Boolean): Seq[T] = values.sortWith(isBefore)
}


