package net.ssanj.describe

import scala.reflect.runtime.universe._
import scala.util.Try

package object api extends ToSymbolOps
                   with    ToTermOps
                   with    ToMethodOps {

  object members extends api.Members

  private[api] def getName(symbol: Symbol): String =
    s"${symbol.name.decodedName.toString}"

  private[api] def getTypeSymbol(t: Type): Option[TypeSymbol] = Try {
    t.typeSymbol.asType
  }.toOption.filterNot(_ == NoSymbol)

  private[api] def getTermSymbol(t: Type): Option[TermSymbol] = Try {
    t.termSymbol.asTerm
  }.toOption.filterNot(_ == NoSymbol)

  private[api] val rm = runtimeMirror(getClass.getClassLoader)
}

