package net.ssanj.describe.api

import scala.reflect.runtime.universe._

trait ToMemberOps {

  private[api] def toMemberOps(tpe: Type) = new MemberOps {

    lazy val members = tpe.members

    lazy val asClass: Option[ClassInfo] = {
      getTypeSymbol(tpe).
        collect { case ts if ts.isClass => ClassInfo(ts.asClass) }
    }

    lazy val finalResultType = tpe.finalResultType

    lazy val resultType = tpe.resultType

    lazy val companion: Option[MemberInfo] = {
      val comp = tpe.dealias.etaExpand.companion
      if (comp == NoType) None else Option(MemberInfo(comp))
    }

    lazy val superclasses: Seq[MemberInfo] = {
      tpe.baseClasses.collect {
        case bc if bc.isClass => MemberInfo(bc.asClass.toType)
      }
    }

    lazy val subclasses: Seq[MemberInfo] = {
      val typeSymbol = tpe.typeSymbol
      if (typeSymbol.isClass) {
        typeSymbol.
        asClass.
        knownDirectSubclasses.
        collect {
          case sc if sc.isClass => MemberInfo(sc.asClass.toType)
        }.toSeq
      }else Seq.empty[MemberInfo]
    }
  }
}