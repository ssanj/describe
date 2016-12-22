package net.ssanj.describe
package api

import org.scalacheck.Properties
import org.scalacheck.{Prop, Gen}

object FilterOpsProps extends Properties("FilterOps") {

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

        miMethods.nonEmpty &&
        miMethodsWithoutAny == miMethods.filterNot(anyMethods.contains)
      }
    }
  }