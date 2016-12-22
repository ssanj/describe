package net.ssanj.describe.api

import scala.reflect.runtime.universe._

final case class MemberInfo(private val ttType: Type)

object MemberInfo {
  implicit def toMemberOpsFromMemberInfo(mi: MemberInfo): MemberOps = toMemberOps(mi.ttType)

  //Going to a TypeSymbol from a Type should always be safe.
  implicit def toSymbolOpsFromMemberInfo(mi: MemberInfo): SymbolOps = toSymbolOps(mi.ttType.typeSymbol)
}


trait Members {

  def info[T: TypeTag]: MemberInfo = MemberInfo(typeOf[T])

  def info[T: TypeTag](value: T): MemberInfo = MemberInfo(typeOf[T])
}