package net.ssanj.describe.api

import scala.reflect.runtime.universe._

final case class ModuleInfo(private val ms: ModuleSymbol) extends SymbolAttr {
  val symbol = ms.asInstanceOf[Symbol]
}

object ModuleInfo {

  implicit def toSymbolOpsFromModuleInfo(mi: ModuleInfo) = toSymbolOps(mi.ms)

  implicit def toModuleOpsFromModuleInfo(mi: ModuleInfo) = toModuleOps(mi.ms)

  implicit val moduleInfoShow: Show[ModuleInfo] =
    Show.create[ModuleInfo]{ mi =>
      val impl = modifiers(mi.isImplicit,           "implicit")
      val dep  = modifiers(isDeprecated(mi.symbol), "[deprecated]")

      val moduleString = mi.moduleClass.map(_.asReflectType.toString).getOrElse(mi.name + ".type")
      s"${dep}${impl}${moduleString}"
    }
}
