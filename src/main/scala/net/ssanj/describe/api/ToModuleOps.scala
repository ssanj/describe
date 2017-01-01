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
  }
}