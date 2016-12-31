package net.ssanj.describe
package api

import scala.reflect.runtime.{universe => u}

trait ClassOps {

  val isSealed: Boolean

  val isTrait: Boolean

  val isCaseClass: Boolean

  val isDerivedValueClass: Boolean

  val isPrimitive: Boolean

  val isNumeric: Boolean

  val isAliasType: Boolean

  val asType: MemberInfo

  val asReflectType: u.Type

  val subclasses: Seq[ClassInfo]

  val typeParams: Seq[MemberInfo]
}