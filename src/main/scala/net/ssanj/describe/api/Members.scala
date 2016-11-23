package net.ssanj.describe.api

import scala.reflect.runtime.universe._

  trait Show[T] {
    def show(value: T): String
  }

  class Format[T](values: Seq[T], S: Show[T]) {
    def newline: String = values.map(S.show(_)).mkString("\n")
    def numbered: String = values.zipWithIndex.map { case (v, i) => "%2d. %s".format(i + 1, S.show(v)) }.mkString("\n")
  }

  class Print(value: String) {
    def print(): Unit = println(value)
  }

  final case class ParamInfo(private val sm: Symbol) {
    lazy val paramName: String = s"${sm.name.decodedName.toString}"

    lazy val  paramType: Type = sm.typeSignature
  }

  final case class MethodInfo(private val ms: MethodSymbol) {

    lazy val paramLists: List[List[ParamInfo]] = ms.paramLists.map(_.map(ParamInfo))

    lazy val methodName: String = s"${ms.name.decodedName.toString}"

    lazy val returnType: MemberInfo = MemberInfo(ms.returnType)

    lazy val isConstructor: Boolean = ms.isConstructor

    lazy val isImplicit: Boolean = ms.isImplicit
  }

  final case class ClassInfo(private val cs: ClassSymbol) {
    lazy val className: String = s"${cs.name.decodedName.toString}"

    lazy val isImplicit = cs.isImplicit

    lazy val isSealed = cs.isSealed

    lazy val isTrait = cs.isTrait

    lazy val isCaseClass = cs.isCaseClass

    lazy val isDerivedValueClass = cs.isDerivedValueClass

    lazy val isPrimitive = cs.isPrimitive

    lazy val isNumeric = cs.isNumeric

    lazy val isAliasType = cs.isAliasType

    lazy val subclasses = cs.knownDirectSubclasses.collect { case c if c.isType => ClassInfo(c.asClass) }
  }

  object MethodInfo {
    implicit val imMethodInfoShow: Show[MethodInfo] = new Show[MethodInfo] {
      def show(value: MethodInfo): String = value.methodName
    }
  }

  final case class MemberInfo(private val ttType: Type) {

    lazy val methods = ttType.members.collect { case m: MethodSymbol => MethodInfo(m) }.toSeq

    lazy val classes = ttType.members.collect { case cs: ClassSymbol => ClassInfo(cs) }.toSeq

    lazy val asClass: Option[ClassInfo] = {
      val ts = ttType.typeSymbol
      if (ts == NoSymbol) None
      else Option(ClassInfo(ts.asClass))
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

    def methodsByName(reg: String): Seq[MethodInfo] = methods.filter(_.methodName.matches(reg))

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

  // private def filterMembers[T: TypeTag, U <: Symbol](filter: PartialFunction[Symbol, U]): Seq[U] = {
  //   members[T].collect(filter).toList.toSeq
  // }

  // def methods[T: TypeTag]: Seq[MethodSymbol] =
  //   members[T].
  //     collect{ case m: MethodSymbol => m }.
  //     toList

  // def types[T: TypeTag]: Seq[TypeSymbol] =
  //   members[T].
  //     collect{ case t: TypeSymbol => t }.
  //     toList

  // def modules[T: TypeTag]: Seq[ModuleSymbol] =
  //   members[T].
  //     collect{ case m: ModuleSymbol => m }.
  //     toList

  // def classes[T: TypeTag]: Seq[ClassSymbol] =
  //     members[T].
  //       collect{ case c: ClassSymbol => c }.
  //       toList
}