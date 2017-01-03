package net.ssanj.describe
package api

import scala.reflect.runtime.{universe => u}

trait ToMethodSignaureOps {

  private[api] def toMethodSignatureOps(mi: MethodInfo) = new MethodSignatureOps {
    lazy val methodSignature: MethodSignature = {
      val isConstructor = mi.name == u.termNames.CONSTRUCTOR.decodedName.toString
      MethodSignature(
        name       =
          //match on name instead of mi.isConstructor to prevent matching $init$ as well.
          if (isConstructor) getOwnerName(mi.symbol).getOrElse(mi.name)
          else mi.name,
        typeParams    = mi.typeParams,
        paramLists    = mi.paramLists,
        returnType    = mi.returnType,
        symbol        = mi.symbol,
        isConstructor = isConstructor
      )
    }
  }
}