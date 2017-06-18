package net.ssanj.describe

trait ClasspathLoader {

  import scala.tools.nsc.util.ClassFileLookup
  import scala.tools.nsc.io.AbstractFile
  import java.io.File

  implicit def toCp(powerClassPath: ClassFileLookup[AbstractFile]): Seq[File] = {
    powerClassPath.asURLs.map{ m => new File(m.getFile) }
  }
}
