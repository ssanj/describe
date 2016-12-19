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
      val comp = tpe.erasure.companion
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

    lazy val symbolFlagValues: Seq[(MethodInfo, Boolean)] = {
      import scala.reflect.runtime.{universe => u}
      import u._
      val typeSymbol = tpe.typeSymbol.asType
      if (typeSymbol.isClass) {
         val mi = MemberInfo(u.typeOf[u.ClassSymbol])
         mi.flagValues(typeSymbol.asClass)
      } else {
        val mi = MemberInfo(u.typeOf[u.TypeSymbol])
        mi.flagValues(typeSymbol)
      }
    }

    lazy val implicitConversionTypes: Seq[MemberInfo] = {

      def getType(): Type = {
        if (MemberInfo(tpe).isModuleClass) {
          companion.map(_.resultType.erasure).getOrElse(tpe.erasure)
        } else tpe.erasure
      }

      val imethods =
        allImplicitMethods.filter { m =>
          val params = m.paramLists.flatten
          params.length == 1 &&
          params.head.paramType.erasure =:= getType() &&
          !(m.returnType.resultType.erasure =:= getType())
        } map (_.returnType)

      val iclasses = allImplicitClasses map (_.asType)

      imethods ++ iclasses
    }
  }
}