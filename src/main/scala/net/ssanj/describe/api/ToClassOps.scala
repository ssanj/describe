package net.ssanj.describe
package api

import scala.reflect.runtime.{universe => u}

trait ToClassOps {
  def toClassOps(cs: u.ClassSymbol) = new ClassOps {

    lazy val isSealed            = cs.isSealed

    lazy val isTrait             = cs.isTrait

    lazy val isCaseClass         = cs.isCaseClass

    lazy val isDerivedValueClass = cs.isDerivedValueClass

    lazy val isPrimitive         = cs.isPrimitive

    lazy val isNumeric           = cs.isNumeric

    lazy val isAliasType         = cs.isAliasType

    lazy val asType              = MemberInfo(asReflectType)

    lazy val asReflectType       = cs.toType

    lazy val subclasses          =
      cs.knownDirectSubclasses.collect { case c if c.isType => ClassInfo(c.asClass) }.toSeq

    lazy val typeParams          =
      cs.typeParams.collect { case tp if tp.isType => MemberInfo(tp.asType.toType) }
  }
}