package net.ssanj.describe
package api

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