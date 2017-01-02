package net.ssanj.describe
package api

import scala.reflect.runtime.{universe => u}

final case class ValInfo(private val ts: u.TermSymbol) {
  val symbol: u.Symbol = ts
  lazy val members     = MemberInfo(ts.typeSignature)
  lazy val rType       = ts.typeSignature
}

object ValInfo {
  implicit def toSymbolOpsFromValInfo(vi: ValInfo) = toSymbolOps(vi.ts)
}
