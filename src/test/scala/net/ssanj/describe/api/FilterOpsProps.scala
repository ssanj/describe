package net.ssanj.describe
package api

import org.scalacheck.Properties
import org.scalacheck.Prop

import PropUtils._

object FilterOpsProps extends Properties("FilterOps") {

  private def assertWithout(
    withMethodsRemoved: Seq[MethodInfo] => Seq[MethodInfo],
    methodsToRemove: Seq[MethodInfo],
    id: String): Prop = {
    Prop.forAll(genMemberInfo) { mi: MemberInfo =>
      Prop.collect(mi.fullName){
        val miMethods        = mi.methods
        val miMethodsRemoved = withMethodsRemoved(miMethods).map(show.MethodNameShow(_))
        val expectedMethods  = miMethods.filterNot(methodsToRemove.contains).map(show.MethodNameShow(_))

        propAssert("Methods should not be empty.")(Seq(true), Seq(miMethods.nonEmpty)) &&
        propAssert(s"Methods should not contain $id methods.")(expectedMethods, miMethodsRemoved)
      }
    }
  }

  property("withoutAny")     = assertWithout(_.withoutAny, anyMethods, "Any")

  property("withoutAnyRef")  = assertWithout(_.withoutAnyRef, anyRefMethods, "AnyRef")

  property("withoutProduct") = assertWithout(_.withoutProduct, productMethods, "Product")

  property("withoutSerial")  = assertWithout(_.withoutSerial, serializableMethods, "Serializable")
}