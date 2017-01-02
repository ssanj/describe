package net.ssanj.describe
package api

trait ModuleOps {

  val moduleClass: Option[ClassInfo]

  val methods: Seq[MethodInfo]

  val modules: Seq[ModuleInfo]

  val classes: Seq[ClassInfo]
}