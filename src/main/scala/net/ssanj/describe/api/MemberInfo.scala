package net.ssanj.describe.api

import scala.reflect.runtime.universe._
import scala.util.{Failure, Success, Try}

final case class MemberInfo(private val ttType: Type) {
  lazy val symbol = ttType.typeSymbol

  //override this to prevent ttType.toString throwing an Excepion in some instances.
  override def toString = symbol.fullName
}

object MemberInfo {
  implicit def toMemberOpsFromMemberInfo(mi: MemberInfo): MemberOps = toMemberOps(mi.ttType)

  //Going to a TypeSymbol from a Type should always be safe.
  implicit def toSymbolOpsFromMemberInfo(mi: MemberInfo): SymbolOps = toSymbolOps(mi.symbol)

  implicit val memberInfoShow: Show[MemberInfo] =
    Show.create[MemberInfo]{ mi =>
      val traitFlag = mi.asClass.map(_.isTrait).getOrElse(false)
      val moduleClassFlag = mi.isModuleClass
      val isModuleClass = modifiers(moduleClassFlag, "[object]", prefix = false)
      val isTrait = modifiers(traitFlag, "[trait]", prefix = false)
      val isPackageClass = modifiers(mi.isPackageClass, "[package]", prefix = false)
      val isClass = modifiers(!traitFlag && !moduleClassFlag, "[class]", prefix = false)
      val isAbstract = modifiers(!traitFlag && mi.isAbstract, "[abstract]", prefix = false)
      s"${mi.fullName}${isAbstract}${isModuleClass}${isTrait}${isClass}${isPackageClass}"
    }

  import scala.math.Ordering

  implicit val memberInfoOrdering: Ordering[MemberInfo] = createOrdering[MemberInfo]
}


trait Members {

  def info[T: TypeTag]: MemberInfo = MemberInfo(typeOf[T])

  def info[T: TypeTag](value: T): MemberInfo = MemberInfo(typeOf[T])


  import scala.tools.nsc.util.ClassFileLookup
  import scala.tools.nsc.io.AbstractFile
  import java.io.File

  def toCp(powerClassPath: ClassFileLookup[AbstractFile]): Seq[File] = {
    powerClassPath.asURLs.map{ m => new File(m.getFile) }
  }

  //TODO: Consider using a Writer here for capturing the output.
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

        names.filterNot(_.contains("$anon")).zipWithIndex.flatMap {
          case (n, i) =>
            if (verbose) println("%03d. %s".format(i, n)) else {}

            val resolvedType: Try[Type] =
              if (n.endsWith("$")) { //try to load as a scala module
                getModuleType(n, verbose) orElse (getClassTypeOfModule(n, verbose))
              } else { //class
                getClassType(n)
              }

             resolvedType match {
              case Success(t)  => Seq(MemberInfo(t))
              case Failure(ex) =>
                if(verbose) println(s"\t${getShortError(ex)}") else {}
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

      def getClassTypeOfModule(className: String, verbos: Boolean): Try[Type] = {
        if (verbose) println(s"\t -> loading as class: $className") else {}
        getClassType(className)
      }

      classpath.filter(p => p.isFile && p.getName.endsWith(".jar")).flatMap(getClassesFromJarFile)
  }

  def getPackageImplicits = getPackageAnything[MethodInfo](_.implicitMethods)

  def getPackageMethods = getPackageAnything[MethodInfo](_.methods)

  def getPackageVals = getPackageAnything[ValInfo](_.vals)

  def getPackageVars = getPackageAnything[VarInfo](_.vars)

  def findPackageVals(f: (MemberInfo, Seq[ValInfo]) => Boolean)(
    classpath: Seq[File], packageFilter: scala.util.matching.Regex, verbose:Boolean):
      Seq[(MemberInfo, Seq[ValInfo])] =
      getPackageVals(classpath, packageFilter, verbose).filter(x => f(x._1, x._2))

  def getPackageSubclasses[T: TypeTag](classpath: Seq[File], packageFilter: scala.util.matching.Regex, verbose: Boolean): Seq[MemberInfo] = {
    val targetType = typeOf[T].erasure
    val members = getPackageClasses(classpath, packageFilter, verbose)
    // members.filter(m => Try(m.resultType.erasure <:< targetType).toOption.fold(false)(identity))
    members.filter(m => tryFold(m.resultType.erasure <:< targetType)(identity, _ => false))
  }

  def getPackageAnything[T](f: MemberInfo => Seq[T]): (Seq[File], scala.util.matching.Regex, Boolean) => Seq[(MemberInfo, Seq[T])] =
    (classpath, packageFilter, verbose) => {
      val members = getPackageClasses(classpath, packageFilter, verbose)
      val results = members.map(mi => tryFold(f(mi))(t => mi -> t, _ => mi -> Seq.empty[T]))
      results.filterNot(_._2.isEmpty)
    }

  //TODO: Add search across packages for methods
  //TODO: Handle `package`.type to get package object contents
}