package net.ssanj.describe
package api

import scala.math.Ordering
import scala.reflect.runtime.{universe => u}

final case class ValInfo(private val ts: u.TermSymbol) {
  val symbol: u.Symbol = ts
  lazy val members     = MemberInfo(ts.typeSignature)
  lazy val rType       = ts.typeSignature
}

object ValInfo {
  implicit def toSymbolOpsFromValInfo(vi: ValInfo) = toSymbolOps(vi.ts)

  implicit def toTermOpsFromValInfo(vi: ValInfo) = toTermOps(vi.ts)

  implicit val valInfoShow: Show[ValInfo] = Show.create[ValInfo](vi => s"val ${vi.name}: ${vi.rType.toString}")

  implicit val valOrdering: Ordering[ValInfo] = createOrdering[ValInfo]
}
