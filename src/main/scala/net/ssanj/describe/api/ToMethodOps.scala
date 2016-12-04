package net.ssanj.describe.api

import scala.reflect.runtime.universe.MethodSymbol

trait ToMethodOps {

  private[api] def toMethodOps(methodSymbol: MethodSymbol) = new MethodOps {

    def isPrimaryConstructor: Boolean = methodSymbol.isPrimaryConstructor

    def isVarargs: Boolean = methodSymbol.isVarargs

    def returnType: MemberInfo = MemberInfo(methodSymbol.returnType)

    def isMethod: Boolean = methodSymbol.isMethod
  }
}