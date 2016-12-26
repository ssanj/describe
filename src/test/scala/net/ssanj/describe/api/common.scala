package net.ssanj.describe
package api

trait Show[T] {
  def show(value: T): String
}

trait LowPriorityShowImplicits {
  implicit def toStringToShow[T]: Show[T] = new Show[T] {
    override def show(value: T): String = value.toString
  }
}

final case class MethodNameShow(value: MethodInfo)

object Show extends LowPriorityShowImplicits {
  def apply[T](implicit S: Show[T]): Show[T] = S

  def create[T](print: T => String): Show[T] = new Show[T] {
    def show(value: T): String = print(value)
  }

  implicit def fromSeq[T](implicit S: Show[T]): Show[Seq[T]] = {
    Show.create[Seq[T]](_.map(v => s"\t${S.show(v)}").mkString("\n"))
  }

  implicit val methodNameShow = Show.create[MethodNameShow](_.value.name)
}

object PropUtils {
  import org.scalacheck.{Gen, Prop}
  import org.scalacheck.Prop.BooleanOperators

  def genMemberInfo: Gen[MemberInfo] =
    Gen.oneOf(
      getPackage[Option[_]].toSeq.
        map( p =>
          p.classes.collect {
            case c if !c.isAbstract &&
                      !c.isJava &&
                      !c.asType.methods.isEmpty => c.asType
          }
        ).flatten
    )

  def propAssert[T: Show](desc: String)(expected: Seq[T], actual: Seq[T]): Prop = {
    val S = implicitly[Show[Seq[T]]]
    (expected == actual) :| (
      s"$desc" +
      s"${System.lineSeparator}In expected but not actual:" +
        s"${System.lineSeparator}${S.show(expected.filterNot(actual.contains))}" +
      s"${System.lineSeparator}In actual but not expected:" +
        s"${System.lineSeparator}${S.show(actual.filterNot(expected.contains))}" +
      s"${System.lineSeparator}In actual and expected:" +
        s"${System.lineSeparator}${S.show(actual.filter(expected.contains))}"
    )
  }
}