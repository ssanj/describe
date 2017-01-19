package net.ssanj.describe.api

import scala.reflect.runtime.universe._

final case class MemberInfo(private val ttType: Type)

object MemberInfo {
  implicit def toMemberOpsFromMemberInfo(mi: MemberInfo): MemberOps = toMemberOps(mi.ttType)

  //Going to a TypeSymbol from a Type should always be safe.
  implicit def toSymbolOpsFromMemberInfo(mi: MemberInfo): SymbolOps = toSymbolOps(mi.ttType.typeSymbol)

  implicit val memberInfoShow: Show[MemberInfo] =
    Show.create[MemberInfo](mi => s"${mi.fullName}")

  import scala.math.Ordering

  implicit val memberInfoOrdering: Ordering[MemberInfo] = createOrdering[MemberInfo]
}


trait Members {

  def info[T: TypeTag]: MemberInfo = MemberInfo(typeOf[T])

  def info[T: TypeTag](value: T): MemberInfo = MemberInfo(typeOf[T])


  import scala.tools.nsc.util.ClassFileLookup
  import scala.tools.nsc.io.AbstractFile

  def findInstances[T: TypeTag](classpath: ClassFileLookup[AbstractFile], p: String => Boolean): Seq[MemberInfo] = {
    import org.clapper.classutil.ClassFinder
    import java.io.File
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
}