package net.ssanj.describe.api

import scala.reflect.runtime.universe.InstanceMirror

trait MethodOps {
  // val exceptions: List[Universe.Symbol]
  val isPrimaryConstructor: Boolean
  val isVarargs: Boolean
  // val paramLists: List[List[Universe.Symbol]]
  // val paramList: List[Universe.Symbol]
  val returnType: MemberInfo
  // val typeParams: List[Universe.Symbol]
  val isMethod: Boolean

  val isParameterless: Boolean

  val isFlag: Boolean

  val hasTypeParams: Boolean

  val paramLists: List[List[ParamInfo]]

  def invoke[T](mirror: InstanceMirror): Option[T]
}