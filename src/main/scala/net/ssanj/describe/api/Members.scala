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

  final case class ParamInfo(sm: Symbol) {
    lazy val paramName: String = s"${sm.name.decodedName.toString}"

    lazy val  paramType: Type = sm.typeSignature
  }

  final case class MethodInfo(ms: MethodSymbol) {

    lazy val paramLists: List[List[ParamInfo]] = ms.paramLists.map(_.map(ParamInfo))

    lazy val methodName: String = s"${ms.name.decodedName.toString}"

    lazy val returnType: Type = ms.returnType

    lazy val isConstructor: Boolean = ms.isConstructor
  }

  object MethodInfo {
    implicit val imMethodInfoShow: Show[MethodInfo] = new Show[MethodInfo] {
      def show(value: MethodInfo): String = value.methodName
    }
  }

  final case class MemberInfo(ttType: Type) {

    lazy val methods = ttType.members.collect { case m: MethodSymbol => MethodInfo(m) }.toSeq

    def methodsByName(reg: String): Seq[MethodInfo] = methods.filter(_.methodName.matches(reg))

    lazy val constructors: Seq[MethodInfo] = methods.filter(_.isConstructor)

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