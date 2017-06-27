package net.ssanj.describe.api

import scala.reflect.runtime.{universe => u}
import org.scalatest.{AppendedClues, Matchers, WordSpecLike}

final class MemberOpsSpec extends Matchers with WordSpecLike with AppendedClues {

  sealed trait TermAssert {
    val name: String
    val tpe: String
    val is: Boolean
  }

  case class Val(val name: String, tpe: String, is: Boolean) extends TermAssert

  case class Var(val name: String, tpe: String, is: Boolean) extends TermAssert

  object TermAssert {
    def fromValInfo: ValInfo => TermAssert = v => Val(v.name, v.rType.resultType.toString, v.isVal)

    def fromVarInfo: VarInfo => TermAssert = v => Var(v.name, v.rType.resultType.toString, v.isVar)
  }

  "MemberOps" should {
    import TermAssert._
    val mi = MemberInfo(u.typeOf[Animal])

    "list vals" in {
      val vals = mi.vals.map(fromValInfo)
      vals should contain theSameElementsAs (
        Seq(
            Val("base",    "scala.Int", true),
            Val("getName", "String",    true),
            Val("name",    "String",    true),
            Val("roar",    "String",    true)
         )
      )

      vals.map(_.name) should not contain theSameElementsAs (Seq("pet", "happiness"))
    }

    "list vars" in {
      val vars = mi.vars.map(fromVarInfo)
      vars should contain theSameElementsAs (
        Seq(
            Var("happiness", "scala.Int", true)
        )
      )

      vars.map(_.name) should not contain theSameElementsAs (
        Seq(
            "base",
            "getName",
            "name",
            "pet",
            "roar"
        )
      )
    }
  }
}