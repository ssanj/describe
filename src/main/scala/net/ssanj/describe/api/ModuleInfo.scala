package net.ssanj.describe.api

import scala.reflect.runtime.universe._

final case class ModuleInfo(private val ms: ModuleSymbol) extends SymbolAttr {
  val symbol = ms.asInstanceOf[Symbol]
  lazy val moduleClass: Option[ClassInfo] = {
    val cs = ms.companion
    if (cs == NoSymbol || !cs.isClass) None
    else Option(ClassInfo(cs.asClass))
  }
}
