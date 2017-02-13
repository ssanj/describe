package net.ssanj.describe
package api
package show

final case class MethodName(value: MethodInfo)

object MethodName {
  implicit val methodNameShow = Show.create[MethodName](_.value.name)

  implicit val methodNameShowOrdering = new Ordering[MethodName] {
    override def compare(m1: MethodName, m2: MethodName): Int = m1.value.name compare m2.value.name
  }
}