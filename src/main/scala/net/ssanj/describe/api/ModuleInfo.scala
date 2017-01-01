package net.ssanj.describe.api

import scala.reflect.runtime.universe._

final case class ModuleInfo(private val ms: ModuleSymbol) extends SymbolAttr {
  val symbol = ms.asInstanceOf[Symbol]
}

object ModuleInfo {

  implicit def toSymbolOpsFromModuleInfo(mi: ModuleInfo) = toSymbolOps(mi.ms)

  implicit def toModuleOpsFromModuleInfo(mi: ModuleInfo) = toModuleOps(mi.ms)
}
