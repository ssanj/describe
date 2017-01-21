package net.ssanj.describe.api

import scala.reflect.runtime.universe._

final case class MemberInfo(private val ttType: Type)

object MemberInfo {
  implicit def toMemberOpsFromMemberInfo(mi: MemberInfo): MemberOps = toMemberOps(mi.ttType)

  //Going to a TypeSymbol from a Type should always be safe.
  implicit def toSymbolOpsFromMemberInfo(mi: MemberInfo): SymbolOps = toSymbolOps(mi.ttType.typeSymbol)

  implicit val memberInfoShow: Show[MemberInfo] =
    // Show.create[MemberInfo](mi => s"${mi.fullName}")
    Show.create[MemberInfo](mi => scala.util.Try(s"${mi.fullName}").getOrElse(mi.resultType.toString))

  import scala.math.Ordering

  implicit val memberInfoOrdering: Ordering[MemberInfo] = createOrdering[MemberInfo]
}


trait Members {

  def info[T: TypeTag]: MemberInfo = MemberInfo(typeOf[T])

  def info[T: TypeTag](value: T): MemberInfo = MemberInfo(typeOf[T])


  import scala.tools.nsc.util.ClassFileLookup
  import scala.tools.nsc.io.AbstractFile
  import java.io.File

  def findInstances[T: TypeTag](classpath: ClassFileLookup[AbstractFile], p: String => Boolean): Seq[MemberInfo] = {
    import org.clapper.classutil.ClassFinder
    getTypeName(typeOf[T]).fold(Seq.empty[MemberInfo]) { name =>
      val cf = ClassFinder(classpath.asURLs.collect{ case u if p(u.getFile) => new File(u.getFile) })
      val impls = ClassFinder.concreteSubclasses(name, cf.getClasses).toList
      impls.flatMap { ci =>
        scala.util.Try {
          val cs = net.ssanj.describe.cm.staticClass(ci.name)
          MemberInfo(cs.toType)
        }.toOption.toSeq
      }
    }
  }

  def filterToClasspath(powerClassPath: ClassFileLookup[AbstractFile], fileFilter: scala.util.matching.Regex): Seq[File] = {
    powerClassPath.asURLs.collect{ case u if fileFilter.findFirstIn(u.getFile).isDefined => new File(u.getFile) }
  }

  def getPackageClasses(classpath: Seq[File], packageFilter: scala.util.matching.Regex): Seq[MemberInfo] = {

      import java.util.jar.JarFile
      import java.util.jar.JarEntry
      import scala.collection.JavaConversions._

      def getClassesFromJarFile(file: File): Seq[MemberInfo] = {
        val jarFile = new JarFile(file)
        //TODO: Should we Try this?
        val entries: Iterator[JarEntry] = jarFile.entries
        val names = entries.collect {
          case e if e.getName.endsWith(".class") =>
            e.getName.replace("/", ".").replace(".class", "")
        }.filter(packageFilter.findFirstIn(_).isDefined).toSeq

        names.flatMap { n =>
          scala.util.Try{
            val tt = net.ssanj.describe.cm.staticClass(n).toType
            tt.typeSymbol.fullName //check that we can get the full name
            tt
          }.map(MemberInfo(_)).toOption.toSeq }
      }

      classpath.filter(p => p.isFile && p.getName.endsWith(".jar")).flatMap(getClassesFromJarFile)
  }

  def findInstances2[T: TypeTag](classpath: Seq[File]): Seq[MemberInfo] =
    // getPackageClasses(classpath, ".*".r).filter(mi => mi.resultType.erasure <:< typeOf[T].erasure)
    getPackageClasses(classpath, ".*".r).filter{ mi =>
      val withAnyGen = scala.util.Try {
        !(mi.resultType.erasure =:= typeOf[T].erasure) && mi.resultType.erasure <:< typeOf[T].erasure
      }

      val withSpecificGen = scala.util.Try{
        !(mi.resultType =:= typeOf[T]) && mi.resultType <:< typeOf[T]
      }

      withAnyGen.orElse(withSpecificGen).getOrElse(false)
    }




}