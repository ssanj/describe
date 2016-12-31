package net.ssanj.describe
package api

trait ToMethodSignatureSeqOps {

  private[api] def toMethodSignatureSeqOps(values: Seq[MethodInfo]) = new MethodSignatureSeqOps {
    def methodSignature = values.map(_.methodSignature)
  }
}