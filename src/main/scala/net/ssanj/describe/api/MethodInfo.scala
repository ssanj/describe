package net.ssanj.describe.api

import scala.reflect.runtime.{universe => u}

final case class MethodInfo(private val ms: u.MethodSymbol) {

  lazy val symbol = ms
}

object MethodInfo {

  implicit def toSymbolOpsFromMethodInfo(methodInfo: MethodInfo) = toSymbolOps(methodInfo.ms)

  implicit def toTermOpsFromMethodInfo(methodInfo: MethodInfo) = toTermOps(methodInfo.ms)

  implicit def toMethodOpsFromMethodInfo(methodInfo: MethodInfo) = toMethodOps(methodInfo.ms)

  implicit def toTransformFromMethodInfo(values: Seq[MethodInfo]): Transform[MethodInfo] =
    new Transform[MethodInfo](values)

  implicit def toMethodSignatureFromMethodInfo(mi: MethodInfo) = toMethodSignatureOps(mi)
}