package net.ssanj.describe.api

import scala.reflect.runtime.universe._
import scala.reflect.ClassTag

trait MemberOps {

  val members: Seq[Symbol]

  val decls: Seq[Symbol]

  lazy val methods = members.collect { case m: MethodSymbol => MethodInfo(m) }.toSeq

  lazy val methodsX =
    methods ++
    implicitConversionTypes.flatMap(_.methods)

  lazy val declared = decls.collect { case m: MethodSymbol => MethodInfo(m) }.toSeq

  lazy val classes = members.collect { case cs: ClassSymbol => ClassInfo(cs) }.toSeq

  lazy val vars = members.collect { case ts: TermSymbol if !ts.isMethod && ts.isVar => VarInfo(ts) }.toSeq

  lazy val vals = members.collect { case ts: TermSymbol if !ts.isMethod && ts.isVal => ValInfo(ts) }.toSeq

  lazy val modules = members.collect { case ms: ModuleSymbol => ModuleInfo(ms) }.toSeq

  val asClass: Option[ClassInfo]

  val finalResultType: Type

  val resultType: Type

  def methodsByParam[T: TypeTag]: Seq[MethodInfo] =
    methods.filter(_.paramLists.exists(_.exists(_.paramType =:= typeOf[T])))

  def methodsByParam2[T: TypeTag, U: TypeTag]: Seq[MethodInfo] =
    methods.filter(_.paramLists.exists { l =>
      val result =
        l.foldLeft((false, false)){(acc, p) => acc match {
          case (false, false) => (p.paramType =:= typeOf[T], p.paramType =:= typeOf[U])
          case (true, false)  => (acc._1, p.paramType =:= typeOf[U])
          case (false, true)  => (p.paramType =:= typeOf[T], acc._2)
          case (true, true)   => acc
        }}
      result._1 && result._2
    })

  def methodsByName(reg: String): Seq[MethodInfo] = methods.filter(_.name.matches(reg))

  def methodsBy(f: MethodInfo => Boolean): Seq[MethodInfo] = methods.filter(f)

  def methodsReturning[T: TypeTag]: Seq[MethodInfo] = methods.filter(_.returnType.finalResultType.erasure =:= typeOf[T].erasure)

  def methodsReturningX[T: TypeTag]: Seq[MethodInfo] = methods.filter(_.returnType.finalResultType.erasure <:< typeOf[T].erasure)

  def methodsReturningTypeParams: Seq[MethodInfo] =
    methods.filter(_.returnType.finalResultType.typeSymbol.isParameter)

  def methodsOfHigherOrder: Seq[MethodInfo] = methods.filter { m =>
    val parameters = m.paramLists.flatten

    def hasHOParam(p: ParamInfo): Boolean =
      getTypeSymbol(p.paramType).
        orElse(getTermSymbol(p.paramType)).
          filter(_.fullName.startsWith("scala.Function")).isDefined

    def hasHOReturnType(memberInfo: MemberInfo): Boolean = {
      memberInfo.fullName.startsWith("scala.Function")
    }

    //check return type first as it will be faster.
    hasHOReturnType(m.returnType) || parameters.exists(hasHOParam)
  }

  val constructors: Seq[MethodInfo]

  lazy val extractors: Seq[MethodInfo] = methods.filter(m => m.name == "unapply" || m.name == "unapplySeq")

  lazy val implicitMethods: Seq[MethodInfo] = methods.filter(_.isImplicit)

  lazy val implicitClasses: Seq[ClassInfo] = classes.filter(_.isImplicit)

  lazy val implicitModules: Seq[ModuleInfo] = modules.filter(_.isImplicit)

  lazy val allImplicitMethods: Seq[MethodInfo] = {
    implicitMethods ++
    companion.toSeq.flatMap(_.implicitMethods)
  }

  lazy val allImplicitClasses: Seq[ClassInfo] = {
    implicitClasses ++
    companion.toSeq.flatMap(_.implicitClasses)
  }

  val companion: Option[MemberInfo]

  val superclasses: Seq[MemberInfo]

  val subclasses: Seq[MemberInfo]

  lazy val flags: Seq[MethodInfo] = methodsBy(_.isFlag)

  def flagValues[T: ClassTag](value: T): Seq[(MethodInfo, Boolean)] = {
    val im = rm.reflect(value)
    val trueFirst = implicitly[math.Ordering[Boolean]].reverse
    flags.
      map(m => m -> m.invoke[Boolean](im)).
      collect{ case (m, Some(result)) => m -> result }.
      sortBy(_._2)(trueFirst)
  }

  val symbolFlagValues: Seq[(MethodInfo, Boolean)]

  val implicitConversionTypes: Seq[MemberInfo]
}