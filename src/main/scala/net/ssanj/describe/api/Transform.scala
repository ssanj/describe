package net.ssanj.describe
package api

trait Functor[T[_]] {
  def map[A, B](functor: T[A], f: A => B): T[B]
}

object Identity {
  implicit val idFunctor: Functor[Identity] = new Functor[Identity] {
    def map[A, B](fa: Identity[A], f: A => B): Identity[B] = f(fa)
  }
}

final class Transform[S[_], T](values: S[T]) {
  def ->>[U](f: T => U)(implicit F: Functor[S]): S[U] = F.map(values, f)

  def transform[U](f: T => U)(implicit F: Functor[S]) = ->>[U](f)(F)
}

object Transform {
  def shortNames(mi: MethodInfo): show.MethodName = show.MethodName(mi)
}
