package net.ssanj.describe.api

import scala.reflect.runtime.universe._

//TODO: Move these methods to a ToClassOps trait
final case class ClassInfo(private val cs: ClassSymbol) {

  val symbol = cs
}

object ClassInfo {
  implicit def toSymbolOpsFromClassInfo(ci: ClassInfo): SymbolOps = toSymbolOps(ci.cs)

  implicit def toClassOpsFromClassInfo(ci: ClassInfo): ClassOps = toClassOps(ci.cs)

  implicit val classInfoShow: Show[ClassInfo] = {

    Show.create[ClassInfo] { ci =>
      val impl = modifiers(ci.isImplicit,           "implicit")
      val fin  = modifiers(ci.isFinal,              "final")
      val sld  = modifiers(ci.isSealed,             "sealed")
      val dep  = modifiers(isDeprecated(ci.symbol), "[deprecated]")

      val classType =
        if (ci.isTrait)             "trait "
        else if (ci.isAbstract)     "abstract class "
        else if (ci.isModuleClass)  "object "
        else                        "class "

      s"${dep}${impl}${fin}${sld}${classType}${ci.asReflectType.toString}"
    }
  }
}