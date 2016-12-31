package net.ssanj.describe
package api

import scala.reflect.runtime.{universe => u}

final case class ParamInfo(private val sm: u.Symbol) {

  val symbol: u.Symbol = sm

  lazy val  paramType: u.Type = sm.typeSignature
}

object ParamInfo {
  implicit def toSymbolOpsFromParamInfo(pi: ParamInfo) = toSymbolOps(pi.symbol)
}
