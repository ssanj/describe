package net.ssanj.describe

package object api extends ToSymbolOps
                   with    ToTermOps
                   with    ToMethodOps {

  object members extends api.Members
  import scala.reflect.runtime.universe._

  private[api] def getName(symbol: Symbol): String =
    s"${symbol.name.decodedName.toString}"
}

