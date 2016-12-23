package net.ssanj.describe
package api

import org.scalacheck.Properties
import org.scalacheck.{Prop, Gen}
import org.scalacheck.Prop.BooleanOperators
import Show.AnyContext

object FilterOpsProps extends Properties("FilterOps") {

  sealed trait MethodName

  implicit val methodNameShow = Show.create[MethodName, MethodInfo](_.name)

  private def am[C, T](desc: String)(expected: T, actual: T)(implicit S: Show[C, T]): Prop = {
    (expected == actual) :| (
      s"$desc" +
      s"${System.lineSeparator}expected:${System.lineSeparator}${S.show(expected)}" +
      s"${System.lineSeparator}actual:${System.lineSeparator}${S.show(actual)}"
    )
  }

  private def genMemberInfo: Gen[MemberInfo] =
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

  property("withoutAny") =
    Prop.forAll(genMemberInfo) { mi: MemberInfo =>
      Prop.collect(mi.fullName){
        val miMethods = mi.methods
        val miMethodsWithoutAny = miMethods.withoutAny
        val expected = miMethods.filterNot(anyMethods.contains)

        am("methods should not be empty")(true, miMethods.nonEmpty)
        am[MethodName, Seq[MethodInfo]]("methods should not contain Any methods.")(expected, miMethodsWithoutAny)
      }
    }

  property("withoutAnyRef") =
    Prop.forAll(genMemberInfo) { mi: MemberInfo =>
      Prop.collect(mi.fullName){
        val miMethods = mi.methods
        val miMethodsWithoutAnyRef = miMethods.withoutAnyRef
        val expected = miMethods.filterNot(anyRefMethods.contains)

        am[AnyContext, Boolean]("methods should not be empty")(true, miMethods.nonEmpty)
        am[MethodName, Seq[MethodInfo]]("methods should not contain AnyRef methods.")(expected, miMethodsWithoutAnyRef)
      }
    }
  }