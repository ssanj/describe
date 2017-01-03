package net.ssanj.describe
package api

import scala.reflect.runtime.{universe => u}

final case class MethodSignature(
  name         : String,
  typeParams   : Seq[MemberInfo],
  paramLists   : List[List[ParamInfo]],
  returnType   : MemberInfo,
  symbol       : u.Symbol,
  isConstructor: Boolean)

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
    else ps.map(p => p.name +
                    {
                      val resultType = p.typeSignature.resultType
                      if (hasEmptySymbol(resultType)) ""
                      else resultType.toString
                    }
               ).mkString("[", ", ", "]")
  }

  private def formatReturnType(mi: MemberInfo): String = {
    val types =
      if (mi.asClass.isEmpty) ""
      else formatTypeParams(mi.asClass.toSeq.flatMap(_.typeParams))

    s"${mi.name}${types}"
  }

  implicit val methodSignatureShow = Show.create[MethodSignature]{ ms =>
    val typeParams = formatTypeParams(ms.typeParams)
    val params     = formatParams(ms.paramLists)
    val returnType = formatReturnType(ms.returnType)
    val dep        = modifiers(isDeprecated(ms.symbol), "[deprecated]")
    val constr     = modifiers(ms.isConstructor, "[constructor]")

    s"${dep}${constr}def ${ms.name}${typeParams}${params}: ${returnType}"
  }

  implicit val methodNameShowOrdering =  new Ordering[MethodSignature] {
    override def compare(m1: MethodSignature, m2: MethodSignature): Int = m1.name compare m2.name
  }
}