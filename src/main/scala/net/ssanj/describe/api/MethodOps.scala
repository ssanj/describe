package net.ssanj.describe.api

trait MethodOps {
  // def exceptions: List[Universe.Symbol]
  def isPrimaryConstructor: Boolean
  def isVarargs: Boolean
  // def paramLists: List[List[Universe.Symbol]]
  // def paramList: List[Universe.Symbol]
  def returnType: MemberInfo
  // def typeParams: List[Universe.Symbol]
  def isMethod: Boolean
}