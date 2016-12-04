package net.ssanj.describe.api

import scala.reflect.runtime.universe._

final case class MethodInfo(private val ms: MethodSymbol) {

  lazy val paramLists: List[List[ParamInfo]] = ms.paramLists.map(_.map(ParamInfo))
}

object MethodInfo {

  implicit def toSymbolOpsForMethodInfo(methodInfo: MethodInfo) = toSymbolOps(methodInfo.ms)

  implicit def toTermOpsForMethodInfo(methodInfo: MethodInfo) = toTermOps(methodInfo.ms)

  implicit def toMethodOpsForMethodInfo(methodInfo: MethodInfo) = toMethodOps(methodInfo.ms)
}

final case class ParamInfo(private val sm: Symbol) extends SymbolAttr {

  val symbol: Symbol = sm

  lazy val  paramType: Type = sm.typeSignature
}
