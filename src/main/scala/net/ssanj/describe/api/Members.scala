package net.ssanj.describe.api

import scala.reflect.runtime.universe._
import scala.util.{Failure, Success, Try}

trait Members {

  def info[T: TypeTag]: MemberInfo = MemberInfo(typeOf[T])

  def info[T: TypeTag](value: T): MemberInfo = MemberInfo(typeOf[T])


  import scala.tools.nsc.util.ClassFileLookup
  import scala.tools.nsc.io.AbstractFile
  import java.io.File

  def toCp(powerClassPath: ClassFileLookup[AbstractFile]): Seq[File] = {
    powerClassPath.asURLs.map{ m => new File(m.getFile) }
  }

  case class PackageSelect(classpath: Seq[File], packageFilter: scala.util.matching.Regex,
    verbose: Boolean)

  object PackageSelect {
    def apply(classpath: Seq[File], packageFilter: scala.util.matching.Regex): PackageSelect =
      PackageSelect(classpath, packageFilter, false)
  }

  //TODO: Consider using a Writer here for capturing the output.
  def getPackageClasses(ps: PackageSelect): Seq[MemberInfo] = {

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
        }.filter(ps.packageFilter.findFirstIn(_).isDefined).toSeq

        names.filterNot(_.contains("$anon")).zipWithIndex.flatMap {
          case (n, i) =>
            if (ps.verbose) println("%03d. %s".format(i, n)) else {}

            val resolvedType: Try[Type] =
              if (n.endsWith("$")) { //try to load as a scala module
                getModuleType(n, ps.verbose) orElse (getClassTypeOfModule(n, ps.verbose))
              } else { //class
                getClassType(n)
              }

             resolvedType match {
              case Success(t)  => Seq(MemberInfo(t))
              case Failure(ex) =>
                if(ps.verbose) println(s"\t${getShortError(ex)}") else {}
                Seq.empty[MemberInfo]
            }
        }
      }

      def getShortError(throwable: Throwable): String = {
        val error = throwable.getMessage
        val javaMirrorIndex = error.indexOf("in JavaMirror")
        val lastSquareBraceIndex = error.lastIndexOf("]")

        if (javaMirrorIndex != -1 &&
            lastSquareBraceIndex != -1 &&
            error.length >= (lastSquareBraceIndex + 1)) {
              error.substring(0, javaMirrorIndex).trim +
              " " +
              error.substring(lastSquareBraceIndex + 1).trim +
              s" - ${throwable.getClass}"
        } else {
          error
        }
      }

      def getModuleType(name: String, verbose: Boolean): Try[Type] = Try {
        //we use moduleSymbol here because:
        //cm.staticClass seems to return a java object for a module
        //which does not contain scala attributes like implicits etc.
        //We can convert the class names to scala class names and
        //use cm.staticModule: scala.Option$ -> scala.Option
        // net.ssanj.describe.cm.moduleSymbol(Class.forName(n)).moduleClass.asClass.toType
        val scalaModuleName = name.substring(0, name.length -1).replace("$", ".")
        if (verbose) println(s"\t -> loading as module: $scalaModuleName") else {}
        val md = net.ssanj.describe.cm.staticModule(scalaModuleName)
        md.moduleClass.asClass.toType
      }

      def getClassType(className: String): Try[Type] = Try {
        net.ssanj.describe.cm.staticClass(className).toType
      }

      def getClassTypeOfModule(className: String, verbose: Boolean): Try[Type] = {
        if (verbose) println(s"\t -> loading as class: $className") else {}
        getClassType(className)
      }

      ps.classpath.filter(p => p.isFile && p.getName.endsWith(".jar")).flatMap(getClassesFromJarFile)
  }

  def getPackageImplicits = getPackageAnything[MethodInfo](_.implicitMethods)

  def getPackageExtractors = getPackageAnything[MethodInfo](_.extractors)

  def getPackageConstructors = getPackageAnything[MethodInfo](_.constructors)

  def findPackageVals(f: ValInfo => Boolean)(ps: PackageSelect):
      Seq[PackageElement[ValInfo]] =
        getPackageAnything[ValInfo](_.vals)(ps).filter(pe => pe.elements.exists(f))

  def getPackageSubclasses[T: TypeTag](ps: PackageSelect): Seq[MemberInfo] = {
    val targetType = typeOf[T].erasure
    val members = getPackageClasses(ps)
    members.filter(m => tryFold(m.resultType.erasure <:< targetType)(identity, _ => false))
  }

  def getPackageAnything[T](f: MemberInfo => Seq[T]): PackageSelect => Seq[PackageElement[T]] =
    ps => {
      val members = getPackageClasses(ps)
      val results = members.map(mi => tryFold(f(mi))(t => mi -> t, _ => mi -> Seq.empty[T]))
      results.filterNot(_._2.isEmpty).map(t => PackageElement[T](t._1, t._2))
    }

  def summarise[T: TypeTag]: TypeSummary = {
    new TypeSummary(MemberInfo(typeOf[T]))
  }

  //TODO: Handle `package`.type to get package object contents
}