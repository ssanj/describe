package net.ssanj.describe.api

import scala.reflect.runtime.universe._

//TODO: Move these methods to a ToClassOps trait
final case class ClassInfo(private val cs: ClassSymbol) {

  val symbol = cs
}

object ClassInfo {
  implicit def toSymbolOpsFromClassInfo(ci: ClassInfo): SymbolOps = toSymbolOps(ci.cs)

  implicit def toClassOpsFromClassInfo(ci: ClassInfo): ClassOps = toClassOps(ci.cs)

  implicit val classInfoShow: Show[ClassInfo] =
    Show.create[ClassInfo] { ci =>
      val classType =
        if (ci.isTrait) "trait"
        else if (ci.isAbstract) "abstract class"
        else if (ci.isModuleClass) "object"
        else "class"
      s"${classType} ${ci.asReflectType.toString}"
    }
}