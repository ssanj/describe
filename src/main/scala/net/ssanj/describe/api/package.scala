package net.ssanj.describe

import scala.reflect.runtime.{universe => u}
import scala.util.Try

package object api extends ToSymbolOps
                   with    ToTermOps
                   with    ToMethodOps
                   with    ToMemberOps
                   with    ToFilterOps
                   with    ToMethodSignaureOps
                   with    ToMethodSignatureSeqOps
                   with    ToClassOps
                   with    ToModuleOps {

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

  private[api] def hasEmptySymbol(t: u.Type): Boolean =
    getTypeSymbol(t).orElse(getTermSymbol(t)).isDefined

  private[api] def getPackage[T: u.TypeTag]: Option[MemberInfo] = for {
    ts <- getTypeSymbol(u.typeOf[T])
    owner = ts.owner
    if (owner.isType && owner.isPackage)
  } yield MemberInfo(owner.asType.toType)

  lazy val deprecatedType = u.typeOf[scala.deprecated]

  private[api] def isDeprecated(symbol: u.Symbol): Boolean = {
    val ann = symbol.annotations
    ann.nonEmpty && ann.head.tree.tpe =:= deprecatedType
  }

  private[api] def modifiers(b: Boolean, whenTrue: String, whenFalse: String = ""): String =
      if (b) s"${whenTrue} " else whenFalse

  private[api] val rm = u.runtimeMirror(getClass.getClassLoader)
}

