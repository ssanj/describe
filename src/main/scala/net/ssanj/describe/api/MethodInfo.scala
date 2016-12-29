package net.ssanj.describe.api

import scala.reflect.runtime.universe._

final case class MethodInfo(private val ms: MethodSymbol) {

  lazy val symbol = ms
}

object MethodInfo {

  implicit def toSymbolOpsFromMethodInfo(methodInfo: MethodInfo) = toSymbolOps(methodInfo.ms)

  implicit def toTermOpsFromMethodInfo(methodInfo: MethodInfo) = toTermOps(methodInfo.ms)

  implicit def toMethodOpsFromMethodInfo(methodInfo: MethodInfo) = toMethodOps(methodInfo.ms)

  implicit def methodInfoToTransform(values: Seq[MethodInfo]): Transform[MethodInfo] =
    new Transform[MethodInfo](values)
}

final case class ParamInfo(private val sm: Symbol) extends SymbolAttr {

  val symbol: Symbol = sm

  lazy val  paramType: Type = sm.typeSignature
}
