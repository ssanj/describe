package net.ssanj.describe.api

import scala.reflect.runtime.{universe => u}
import org.scalatest.{AppendedClues, Matchers, WordSpecLike}

final class MemberOpsMethodNamesSpec extends Matchers with WordSpecLike with AppendedClues {

  "MemberOps" should {
    "find public methods" when {
      "a class has public methods" in {
        val mi          = MemberInfo(u.typeOf[Animal])
        val methods     = mi.methods
        val methodNames = methods.map(_.name)

        methodNames should contain ("pet")
        methodNames should contain ("getName") //converted to method
        methodNames should contain ("roar") //converted to method

        val vals = mi.vals.map(v => (v.name, v.isVal))
        vals should contain theSameElementsAs (Seq("roar" -> true, "getName" -> true))
        vals.map(_._1) should not contain theSameElementsAs (Seq("pet"))

        //why is this found as a method?
        val getNameMethod = methods.filter(_.name == "getName").head
        getNameMethod.isMethod should be (true)
        getNameMethod.isVal should be (false)//What?? no longer a val.
      }
    }
  }
}