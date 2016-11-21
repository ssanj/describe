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

    def methodsByName(reg: String): Seq[MethodInfo] = methods.filter(_.methodName.matches(reg))

    lazy val constructors: Seq[MethodInfo] = methods.filter(_.isConstructor)

    lazy val implicitMethods: Seq[MethodInfo] = methods.filter(_.isImplicit)

    lazy val implicitClasses: Seq[ClassInfo] = classes.filter(_.isImplicit)

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