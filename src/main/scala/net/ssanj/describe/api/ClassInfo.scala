package net.ssanj.describe.api

import scala.reflect.runtime.universe._

final case class ClassInfo(private val cs: ClassSymbol) extends SymbolAttr {

  val symbol: Symbol = cs

  lazy val isImplicit = cs.isImplicit

  lazy val isSealed = cs.isSealed

  lazy val isTrait = cs.isTrait

  lazy val isCaseClass = cs.isCaseClass

  lazy val isDerivedValueClass = cs.isDerivedValueClass

  lazy val isPrimitive = cs.isPrimitive

  lazy val isNumeric = cs.isNumeric

  lazy val isAliasType = cs.isAliasType

  lazy val subclasses = cs.knownDirectSubclasses.collect { case c if c.isType => ClassInfo(c.asClass) }
}