package net.ssanj.describe
package api
package show

final case class MethodNameShow(value: MethodInfo)

object MethodNameShow {
  implicit val methodNameShow = Show.create[MethodNameShow](_.value.name)

  implicit val methodNameShowOrdering =  new Ordering[MethodNameShow] {
    override def compare(m1: MethodNameShow, m2: MethodNameShow): Int = m1.value.name compare m2.value.name
  }
}
