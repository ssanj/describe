package net.ssanj.describe.api

trait TermOps {
  // def accessed: Universe.Symbol
  // def getter: Universe.Symbol
  // def setter: Universe.Symbol

  val isAccessor: Boolean
  val isByNameParam: Boolean
  val isCaseAccessor: Boolean
  val isGetter: Boolean
  val isLazy: Boolean
  val isOverloaded: Boolean
  val isParamAccessor: Boolean
  val isParamWithDefault: Boolean
  val isSetter: Boolean
  val isStable: Boolean
  val isVal: Boolean
  val isVar: Boolean
  val isTerm: Boolean
}