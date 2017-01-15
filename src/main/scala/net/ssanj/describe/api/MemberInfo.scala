package net.ssanj.describe.api

import scala.reflect.runtime.universe._

final case class MemberInfo(private val ttType: Type)

object MemberInfo {
  implicit def toMemberOpsFromMemberInfo(mi: MemberInfo): MemberOps = toMemberOps(mi.ttType)

  //Going to a TypeSymbol from a Type should always be safe.
  implicit def toSymbolOpsFromMemberInfo(mi: MemberInfo): SymbolOps = toSymbolOps(mi.ttType.typeSymbol)

  implicit val memberInfoShow: Show[MemberInfo] =
    Show.create[MemberInfo](mi => s"${mi.fullName}")

  import scala.math.Ordering

  implicit val memberInfoOrdering: Ordering[MemberInfo] = createOrdering[MemberInfo]
}


trait Members {

  def info[T: TypeTag]: MemberInfo = MemberInfo(typeOf[T])

  def info[T: TypeTag](value: T): MemberInfo = MemberInfo(typeOf[T])
}