package net.ssanj.describe.api

import scala.reflect.runtime.{universe => u}
import org.scalatest.{AppendedClues, Matchers, WordSpecLike}

final class MemberInfoSpec extends Matchers with WordSpecLike with AppendedClues {
  "MemberInfo" should {
    "find a companion"  when {
      "a class has a companion object" in {
        assertCompanion(u.typeOf[Option[_]],    u.typeOf[Option.type], "option2Iterable")
        assertCompanion(u.typeOf[Either[_, _]], u.typeOf[Either.type], "cond")
      }

      "an object has a companion class" in {
        assertCompanion(u.typeOf[Option.type], u.typeOf[Option[_]], "getOrElse")
        assertCompanion(u.typeOf[Either.type], u.typeOf[Either[_, _]], "joinRight")
      }
    }
  }

  it should {
    "not find a companion" when {
      "a class does not have a companion object" in {
        val mi = MemberInfo(u.typeOf[NoCompanionObject])
        mi.companion should be (None)
      }

      "an object does not have a companion class" in {
        val mi = MemberInfo(u.typeOf[NoCompanionClass.type])
        mi.companion should be (None)
      }
    }
  }

  it should {
    "find implicit conversion types" when {

      def assertOptionImplicitConversionTypes(mi: MemberInfo): Unit = {
        val ict = mi.implicitConversionTypes
        ict should have size 1
        ict.head.resultType.erasure =:= u.typeOf[Iterable[_]].erasure

      }

      "when called from a class" in {
        assertOptionImplicitConversionTypes(MemberInfo(u.typeOf[Option[_]]))
      }

      "when called from an object" in {
        assertOptionImplicitConversionTypes(MemberInfo(u.typeOf[Option.type]))
      }
    }
  }

  private def assertCompanion(sourceT: u.Type, companionT: u.Type, methodName: String): Unit = {
    val mi   = MemberInfo(sourceT)
    val comp = mi.companion

    val Some(compMi) = comp

    val methodFromReflection = companionT.member(u.TermName(methodName))
    val methodFromDescribe   = compMi.methodsByName(methodName)

    {methodFromDescribe should have size 1} withClue(
      s"Could not find method $methodName on type: $companionT"
    )

    methodFromReflection should be (methodFromDescribe.head.symbol)
  }
}