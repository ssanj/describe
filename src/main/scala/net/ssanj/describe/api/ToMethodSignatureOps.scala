package net.ssanj.describe
package api

trait ToMethodSignaureOps {

  private[api] def toMethodSignatureOps(mi: MethodInfo) = new MethodSignatureOps {
    lazy val methodSignature: MethodSignature =
      MethodSignature(
        name       = mi.name,
        typeParams = mi.typeParams,
        paramLists = mi.paramLists,
        returnType = mi.returnType
      )
  }
}