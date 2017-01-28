package net.ssanj.describe.api

import scala.reflect.runtime.universe._

final case class MemberInfo(private val ttType: Type) {
  lazy val symbol = ttType.typeSymbol
}

object MemberInfo {
  implicit def toMemberOpsFromMemberInfo(mi: MemberInfo): MemberOps = toMemberOps(mi.ttType)

  //Going to a TypeSymbol from a Type should always be safe.
  implicit def toSymbolOpsFromMemberInfo(mi: MemberInfo): SymbolOps = toSymbolOps(mi.symbol)

  implicit val memberInfoShow: Show[MemberInfo] =
    Show.create[MemberInfo](mi => s"${mi.fullName}")
    // Show.create[MemberInfo](mi => scala.util.Try(s"${mi.fullName}").getOrElse(mi.resultType.toString))

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

  def toCp(powerClassPath: ClassFileLookup[AbstractFile]): Seq[File] = {
    powerClassPath.asURLs.map{ m => new File(m.getFile) }
  }

  def getPackageClasses(classpath: Seq[File], packageFilter: scala.util.matching.Regex,
    verbose: Boolean): Seq[MemberInfo] = {

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
          if (verbose) println(n)
          scala.util.Try{
            if (n.endsWith("$")) { //module
              //we use moduleSymbol here because:
              //cm.staticClass seems to return a java object for a module
              //which does not contain scala attributes like implicits etc.
              //also cm.staticModule does not return any useful information. Only MODULE$
              //alternatively, we can convert the class names to scala class names and
              //use cm.staticModule: scala.Option$ -> scala.Option
              // net.ssanj.describe.cm.moduleSymbol(Class.forName(n)).moduleClass.asClass.toType
              val scalaModuleName = n.substring(0, n.length -1).replace("$", ".")
              if (verbose) println(s"\t -> $scalaModuleName") else {}
              val md = net.ssanj.describe.cm.staticModule(scalaModuleName)
              md.typeSignature.toString //verify that the signature is valid
              md.moduleClass.asClass.toType
            } else { //class
              val cl = net.ssanj.describe.cm.staticClass(n)
              cl.typeSignature.toString //verify that the signature is valid
              cl.toType
            }
          }.map(MemberInfo(_)).toOption.toSeq }
      }

      classpath.filter(p => p.isFile && p.getName.endsWith(".jar")).flatMap(getClassesFromJarFile)
  }

  def getPackageImplicits = getPackageAnything[MethodInfo](_.allImplicitMethods)

  def getPackageMethods = getPackageAnything[MethodInfo](_.methods)

  def getPackageAnything[T](f: MemberInfo => Seq[T]): Seq[File] => scala.util.matching.Regex => Boolean => Seq[(MemberInfo, Seq[T])] =
    classpath => packageFilter => verbose => {
      val members = getPackageClasses(classpath, packageFilter, verbose)
      val results = members.map(mi => scala.util.Try(f(mi)).map(t => mi -> t).getOrElse(mi -> Seq.empty[T]))
      results.filterNot(_._2.isEmpty)
    }

  //TODO: Add search across packages for methods
  //TODO: Handle `package`.type to get package object contents

  def findInstances2[T: TypeTag](classpath: Seq[File], packageFilter: scala.util.matching.Regex,
    verbose: Boolean): Seq[MemberInfo] =
    getPackageClasses(classpath, packageFilter, verbose).filter{ mi =>
      val withAnyGen = scala.util.Try {
        !(mi.resultType.erasure =:= typeOf[T].erasure) && mi.resultType.erasure <:< typeOf[T].erasure
      }

      val withSpecificGen = scala.util.Try{
        !(mi.resultType =:= typeOf[T]) && mi.resultType <:< typeOf[T]
      }

      withSpecificGen.orElse(withAnyGen).getOrElse(false)
    }
}