package net.ssanj.describe

import scala.reflect.runtime.{universe => u}
import scala.util.Try

package object api extends ToSymbolOps
                   with    ToTermOps
                   with    ToMethodOps
                   with    ToMemberOps
                   with    ToFilterOps {

  object members extends api.Members

  private[api] def getName(symbol: u.Symbol): String =
    s"${symbol.name.decodedName.toString}"

  private[api] def getTypeName(tpe: u.Type): Option[String] =
  getTypeSymbol(tpe) orElse(getTermSymbol(tpe)) map(_.fullName)

  private[api] def getTypeSymbol(t: u.Type): Option[u.TypeSymbol] = Try {
    t.typeSymbol.asType
  }.toOption.filterNot(_ == u.NoSymbol)

  private[api] def getTermSymbol(t: u.Type): Option[u.TermSymbol] = Try {
    t.termSymbol.asTerm
  }.toOption.filterNot(_ == u.NoSymbol)

  private[api] def getPackage[T: u.TypeTag]: Option[MemberInfo] = for {
    ts <- getTypeSymbol(u.typeOf[T])
    owner = ts.owner
    if (owner.isType && owner.isPackage)
  } yield MemberInfo(owner.asType.toType)

  private[api] val rm = u.runtimeMirror(getClass.getClassLoader)
}

