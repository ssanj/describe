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

  implicit def toMethodSignatureSeqFromMethodInfoSeq(values: Seq[MethodInfo]) =
    toMethodSignatureSeqOps(values)

  implicit val methodInfoShow: Show[MethodInfo] =
    Show.create[MethodInfo](mi => implicitly[Show[MethodSignature]].show(mi.methodSignature))

  import scala.math.Ordering

  implicit val methodInfoOrdering: Ordering[MethodInfo] = new Ordering[MethodInfo] {
    override def compare(m1: MethodInfo, m2: MethodInfo): Int =
      implicitly[Ordering[MethodSignature]].compare(m1.methodSignature, m2.methodSignature)
  }
}