package net.ssanj.describe.api

import scala.reflect.runtime.universe.TermSymbol

trait ToTermOps {

  private[api] def toTermOps(symbol: TermSymbol) = new TermOps {

    val isAccessor: Boolean         = symbol.isAccessor

    val isByNameParam: Boolean      = symbol.isByNameParam

    val isCaseAccessor: Boolean     = symbol.isCaseAccessor

    val isGetter: Boolean           = symbol.isGetter

    val isLazy: Boolean             = symbol.isLazy

    val isOverloaded: Boolean       = symbol.isOverloaded

    val isParamAccessor: Boolean    = symbol.isParamAccessor

    val isParamWithDefault: Boolean = symbol.isParamWithDefault

    val isSetter: Boolean           = symbol.isSetter

    val isStable: Boolean           = symbol.isStable

    val isVal: Boolean              = symbol.isVal

    val isVar: Boolean              = symbol.isVar

    val isTerm: Boolean             = symbol.isTerm
  }
}