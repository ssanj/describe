package net.ssanj.describe
package api

import scala.reflect.runtime.{universe => u}

trait ToModuleOps {
  def toModuleOps(ms: u.ModuleSymbol) = new ModuleOps {

    lazy val moduleClass: Option[ClassInfo] = {
      val cs = ms.moduleClass
      if (cs == u.NoSymbol || !cs.isClass) None
      else Option(ClassInfo(cs.asClass))
    }

    private lazy val moduleClassSeq = moduleClass.toSeq

    private def fromMemberInfo[T](f: MemberInfo => Seq[T]): Seq[T] =
      moduleClassSeq.flatMap(f.compose((c:ClassInfo) => c.asType))

    lazy val members = moduleClass.map(_.members)

    lazy val membersSeq = members.toSeq
  }
}