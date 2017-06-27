package net.ssanj.describe.api

import scala.reflect.runtime.{universe => u}
import org.scalatest.{AppendedClues, Matchers, WordSpecLike}

final class MemberOpsMethodNamesSpec extends Matchers with WordSpecLike with AppendedClues {

  "MemberOps" should {
    "find public methods" when {
      "a class has public methods" in {
        val mi          = MemberInfo(u.typeOf[Animal])

        //vals
        val vals = mi.vals.map(v => (v.name, v.isVal))
        vals should contain theSameElementsAs (Seq("roar" -> true, "getName" -> true, "base" -> true, "name" -> true))
        vals.map(_._1) should not contain theSameElementsAs (Seq("pet", "happiness"))


        val cons = mi.constructors
        cons.length should be (1)
        val primary = cons.head
        primary.paramLists.foreach { args =>
          args.length should be (2)
          val pFirst  = args(0)
          val pSecond = args(1)
          pFirst.name should be ("base")
          pFirst.paramType =:= u.typeOf[Int] should be (true)
          pSecond.name should be ("name")
          pSecond.paramType =:= u.typeOf[String] should be (true)
        }

        //methods
        val methods     = mi.methods
        val methodNames = methods.map(_.name)
        methodNames should contain ("pet")
        methodNames should contain ("getName")
        methodNames should contain ("roar")
        methodNames should contain ("happiness")
        // methodNames should contain ("base")//constructor params are not converted to methods?
        // methodNames should contain ("name")

        assertMethod[String](methods, "getName", _ should be ('empty))
        assertMethod[String](methods, "roar", _ should be ('empty))
        assertMethod[Int](methods, "happiness", _ should be ('empty))

        assertMethod[Int](methods, "pet", p => {
          p.length should be (1)
          val argList = p.head
          argList.length should be (1)
          val paramVal = argList.head
          paramVal.name should be ("pats")
          paramVal.paramType =:= u.typeOf[Int] should be (true)
        })
      }

      def assertMethod[T: u.TypeTag](methods: Seq[MethodInfo], name: String, paramAssert: List[List[ParamInfo]] => Unit): Unit = {
        val methodOp = methods.find(_.name == name)
        methodOp.isDefined should be (true)

        val m = methodOp.get
        m.returnType.resultType =:= u.typeOf[T] should be (true)
        m.isMethod should be (true)
        m.isVal should be (false)
        m.isVar should be (false)
        paramAssert(m.paramLists)
      }
    }
  }
}