package net.ssanj.describe.api

import scala.reflect.runtime.universe._

final case class MemberInfo(private val ttType: Type) {

  lazy val name = getName(ttType.typeSymbol)

  private lazy val members = ttType.members

  lazy val methods = members.collect { case m: MethodSymbol => MethodInfo(m) }.toSeq

  lazy val classes = members.collect { case cs: ClassSymbol => ClassInfo(cs) }.toSeq

  lazy val vars = members.collect { case ts: TermSymbol if !ts.isMethod && ts.isVar => VarInfo(ts) }.toSeq

  lazy val vals = members.collect { case ts: TermSymbol if !ts.isMethod && ts.isVal => ValInfo(ts) }.toSeq

  lazy val modules = members.collect { case ms: ModuleSymbol => ModuleInfo(ms) }.toSeq

  lazy val asClass: Option[ClassInfo] = {
    val ts = ttType.typeSymbol
    if (ts == NoSymbol || !ts.isClass) None
    else Option(ClassInfo(ts.asClass.asClass))
  }

  lazy val finalResultType = ttType.finalResultType

  lazy val resultType = ttType.resultType

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

  def methodsReturning[T: TypeTag]: Seq[MethodInfo] = methods.filter(_.returnType.finalResultType =:= typeOf[T])

  def methodsReturningTypeParams: Seq[MethodInfo] =
    methods.filter(_.returnType.finalResultType.typeSymbol.isParameter)

  lazy val constructors: Seq[MethodInfo] = methods.filter(_.isConstructor)

  lazy val implicitMethods: Seq[MethodInfo] = methods.filter(_.isImplicit)

  lazy val implicitClasses: Seq[ClassInfo] = classes.filter(_.isImplicit)

  lazy val allImplicitMethods: Seq[MethodInfo] = {
    implicitMethods ++
    companion.toSeq.flatMap(_.implicitMethods)
  }

  lazy val allImplicitClasses: Seq[ClassInfo] = {
    implicitClasses ++
    companion.toSeq.flatMap(_.implicitClasses)
  }

  lazy val companion: Option[MemberInfo] = {
    val comp = ttType.dealias.etaExpand.companion
    if (comp == NoType) None else Option(MemberInfo(comp))
  }

  lazy val superclasses: Seq[MemberInfo] = {
    ttType.baseClasses.collect {
      case bc if bc.isClass => MemberInfo(bc.asClass.toType)
    }
  }

  lazy val subclasses: Seq[MemberInfo] = {
      val typeSymbol = ttType.typeSymbol
      if (typeSymbol.isClass) {
        typeSymbol.
        asClass.
        knownDirectSubclasses.
        collect {
          case sc if sc.isClass => MemberInfo(sc.asClass.toType)
        }.toSeq
      }else Seq.empty[MemberInfo]
  }
}


trait Members {

  def info[T: TypeTag]: MemberInfo = MemberInfo(typeOf[T])

  def info[T: TypeTag](value: T): MemberInfo = MemberInfo(typeOf[T])
}