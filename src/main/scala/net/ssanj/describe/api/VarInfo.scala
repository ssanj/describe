package net.ssanj.describe
package api

import scala.reflect.runtime.{universe => u}

final case class VarInfo(private val ts: u.TermSymbol) {
  val symbol: u.Symbol = ts
  lazy val members     = MemberInfo(ts.typeSignature)
  lazy val rType       = ts.typeSignature
}


object VarInfo {
  implicit def toSymbolOpsFromVarInfo(vi: VarInfo) = toSymbolOps(vi.ts)

  implicit val varInfoShow: Show[VarInfo] = Show.create[VarInfo](vi => s"var ${vi.name}: ${vi.rType.toString}")
}