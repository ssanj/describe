package net.ssanj.describe.api

import scala.reflect.runtime.universe._

final case class VarInfo(private val ts: TermSymbol) extends SymbolAttr  {
  val symbol: Symbol = ts
  lazy val `type` = MemberInfo(ts.typeSignature)
}

final case class ValInfo(private val ts: TermSymbol) extends SymbolAttr  {
  val symbol: Symbol = ts
  lazy val `type` = MemberInfo(ts.typeSignature)
}
