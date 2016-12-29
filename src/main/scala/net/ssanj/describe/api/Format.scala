package net.ssanj.describe.api

import scala.math.Ordering

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

final case class MethodNameShow(value: MethodInfo)

object MethodNameShow {
  implicit val methodNameShow = Show.create[MethodNameShow](_.value.name)

  implicit val methodNameShowOrdering =  new Ordering[MethodNameShow] {
    override def compare(m1: MethodNameShow, m2: MethodNameShow): Int = m1.value.name compare m2.value.name
  }
}

final class Transform[T](values: Seq[T]) {
  def ->>[U](f: T => U): Seq[U] = values map f

  def transform[U] = ->>[U](_)
}


object Transform {
  def shortNames(mi: MethodInfo): MethodNameShow = MethodNameShow(mi)
}

final class Sorted[T](values: Seq[T], S: Show[T]) {
  def sortAlpha(implicit ord: scala.math.Ordering[T]): Format[T] = new Format(values.sorted, S)
  //sort by parents - may need to display parent info as well.
  def sortBy(isBefore: (T, T) => Boolean): Format[T] = new Format(values.sortWith(isBefore), S)
}

final class Format[T](values: Seq[T], S: Show[T]) {
  def nl: String = values.map(S.show(_)).mkString("\n")
  def num(): String = values.zipWithIndex.map { case (v, i) => "%2d. %s".format(i + 1, S.show(v)) }.mkString("\n")
}

class Print(value: String) {
  def print(): Unit = println(value)
}

