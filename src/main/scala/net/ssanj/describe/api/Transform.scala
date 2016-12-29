package net.ssanj.describe
package api

final class Transform[T](values: Seq[T]) {
  def ->>[U](f: T => U): Seq[U] = values map f

  def transform[U] = ->>[U](_)
}

object Transform {
  def shortNames(mi: MethodInfo): show.MethodNameShow = show.MethodNameShow(mi)
}
