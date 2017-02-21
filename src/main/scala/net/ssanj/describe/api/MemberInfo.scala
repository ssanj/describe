package net.ssanj.describe.api

import scala.reflect.runtime.universe._

final case class MemberInfo(private val ttType: Type) {
  lazy val symbol = ttType.typeSymbol

  //override this to prevent ttType.toString throwing an Excepion in some instances.
  override def toString = symbol.fullName
}

object MemberInfo {
  implicit def toMemberOpsFromMemberInfo(mi: MemberInfo): MemberOps = toMemberOps(mi.ttType)

  //Going to a TypeSymbol from a Type should always be safe.
  implicit def toSymbolOpsFromMemberInfo(mi: MemberInfo): SymbolOps = toSymbolOps(mi.symbol)

  implicit def toClassSymbolOpsFromMemberInfo(mi: MemberInfo): ClassOps = toClassOps(mi.symbol.asClass)

  implicit val memberInfoShow: Show[MemberInfo] =
    Show.create[MemberInfo]{ mi =>
      val traitFlag = mi.asClass.map(_.isTrait).getOrElse(false)
      val moduleClassFlag = mi.isModuleClass
      val isModuleClass = modifiers(moduleClassFlag, "[object]", prefix = false)
      val isTrait = modifiers(traitFlag, "[trait]", prefix = false)
      val isPackageClass = modifiers(mi.isPackageClass, "[package]", prefix = false)
      val isClass = modifiers(!traitFlag && !moduleClassFlag, "[class]", prefix = false)
      val isAbstract = modifiers(!traitFlag && mi.isAbstract, "[abstract]", prefix = false)
      s"${mi.fullName}${isAbstract}${isModuleClass}${isTrait}${isClass}${isPackageClass}"
    }

  import scala.math.Ordering

  implicit val memberInfoOrdering: Ordering[MemberInfo] = createOrdering[MemberInfo]
}