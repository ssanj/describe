package net.ssanj.describe.api

import scala.reflect.runtime.universe._

final case class MethodInfo(private val ms: MethodSymbol) {

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

final case class MethodSignature(
  name: String,
  typeParams: Seq[MemberInfo],
  paramLists: List[List[ParamInfo]],
  returnType: MemberInfo)

object MethodSignature {

  private def formatParams(ps: Seq[Seq[ParamInfo]]): String = {
    if (ps.isEmpty) ""
    else ps.map(formatParam).mkString
  }

   private def formatParam(ps: Seq[ParamInfo]): String = {
    if (ps.isEmpty) ""
    else ps.map(p => s"${p.name}: ${p.paramType}").mkString("(", ", ", ")")
  }

  private def formatTypeParams(ps: Seq[MemberInfo]): String = {
    if (ps.isEmpty) ""
    else ps.map(_.name).mkString("[", ", ", "]")
  }

  implicit val methodSignatureShow = Show.create[MethodSignature]{ ms =>
    val typeParams = formatTypeParams(ms.typeParams)
    val params     = formatParams(ms.paramLists)

    s"def ${ms.name}${typeParams}${params}: ${ms.returnType.name}"
  }

    implicit val methodNameShowOrdering =  new Ordering[MethodSignature] {
      override def compare(m1: MethodSignature, m2: MethodSignature): Int = m1.name compare m2.name
    }

}

final case class ParamInfo(private val sm: Symbol) extends SymbolAttr {

  val symbol: Symbol = sm

  lazy val  paramType: Type = sm.typeSignature
}
