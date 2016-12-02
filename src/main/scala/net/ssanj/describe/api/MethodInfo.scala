package net.ssanj.describe.api

import scala.reflect.runtime.universe._

final case class MethodInfo(private val ms: MethodSymbol) extends SymbolAttr  {

  val symbol: Symbol = ms

  lazy val paramLists: List[List[ParamInfo]] = ms.paramLists.map(_.map(ParamInfo))

  lazy val returnType: MemberInfo = MemberInfo(ms.returnType)

  lazy val isConstructor: Boolean = ms.isConstructor

  lazy val isImplicit: Boolean = ms.isImplicit
}

final case class ParamInfo(private val sm: Symbol) extends SymbolAttr {

  val symbol: Symbol = sm

  lazy val  paramType: Type = sm.typeSignature
}
