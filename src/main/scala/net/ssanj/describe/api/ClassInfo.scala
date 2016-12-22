package net.ssanj.describe.api

import scala.reflect.runtime.universe._

//TODO: Move these methods to a ToClassOps trait
final case class ClassInfo(private val cs: ClassSymbol) {

  val symbol: Symbol = cs

  lazy val isImplicit = cs.isImplicit

  lazy val isSealed = cs.isSealed

  lazy val isTrait = cs.isTrait

  lazy val isCaseClass = cs.isCaseClass

  lazy val isDerivedValueClass = cs.isDerivedValueClass

  lazy val isPrimitive = cs.isPrimitive

  lazy val isNumeric = cs.isNumeric

  lazy val isAliasType = cs.isAliasType

  lazy val asType = MemberInfo(symbol.asType.toType)

  lazy val asReflectType = symbol.asType.toType

  lazy val subclasses = cs.knownDirectSubclasses.collect { case c if c.isType => ClassInfo(c.asClass) }
}

object ClassInfo {
  implicit def toSymbolOpsFromClassInfo(ci: ClassInfo): SymbolOps = toSymbolOps(ci.cs)
}