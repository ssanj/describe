package net.ssanj.describe

trait ClasspathLoader {

  import scala.tools.nsc.util.ClassPath
  import scala.tools.nsc.io.AbstractFile
  import java.io.File

  implicit def toCp(powerClassPath: ClassPath): Seq[File] = {
    powerClassPath.asURLs.map{ m => new File(m.getFile) }
  }
}
