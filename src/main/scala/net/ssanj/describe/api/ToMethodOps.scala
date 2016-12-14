package net.ssanj.describe.api

import scala.reflect.runtime.universe.MethodSymbol
import scala.reflect.runtime.universe.InstanceMirror
import scala.reflect.runtime.universe.definitions.BooleanTpe
import scala.util.Try

trait ToMethodOps {

  private[api] def toMethodOps(methodSymbol: MethodSymbol) = new MethodOps {

    lazy val isPrimaryConstructor: Boolean = methodSymbol.isPrimaryConstructor

    lazy val isVarargs: Boolean = methodSymbol.isVarargs

    lazy val returnType: MemberInfo = MemberInfo(methodSymbol.returnType)

    lazy val isMethod: Boolean = methodSymbol.isMethod

    lazy val isParameterless: Boolean = methodSymbol.paramLists.isEmpty

    lazy val hasTypeParams: Boolean = methodSymbol.typeParams.nonEmpty

    lazy val isFlag: Boolean =
      isParameterless &&
      !hasTypeParams && /* filters out methods like isInstanceOf[T] => Boolean */
      returnType.resultType =:= BooleanTpe

    def invoke[T](mirror: InstanceMirror): Option[T] = {
      val result = mirror.reflectMethod(methodSymbol).apply()
      Try(result.asInstanceOf[T]).toOption
    }
  }
}