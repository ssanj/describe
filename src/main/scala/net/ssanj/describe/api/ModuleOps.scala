package net.ssanj.describe
package api

trait ModuleOps {

  val moduleClass: Option[ClassInfo]

  val members: Option[MemberInfo]

  val membersSeq: Seq[MemberInfo]
}