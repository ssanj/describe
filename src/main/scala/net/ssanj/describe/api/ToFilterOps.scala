package net.ssanj.describe.api

import scala.reflect.runtime.{universe => u}

trait ToFilterOps {

  object FilterOps {

    lazy val anyTypeName = getTypeName(u.typeOf[Any])

    lazy val anyRefTypeName = getTypeName(u.typeOf[AnyRef])

    lazy val productName = getTypeName(u.typeOf[Product])

    lazy val scalaSerializableName = getTypeName(u.typeOf[Serializable])

    lazy val javaSerializableName = getTypeName(u.typeOf[java.io.Serializable])
  }

  //TODO: abstract over Seq -> [C <: Traversable]
  implicit class FilterOps(val mi: Seq[MethodInfo]) {

    import FilterOps._

    private def without(op: Option[String]): Seq[MethodInfo] =
      mi.filterNot(m => op.exists(_ == m.symbol.owner.fullName))

    lazy val withoutAny: Seq[MethodInfo] = without(anyTypeName)

    lazy val withoutAnyRef: Seq[MethodInfo] = without(anyRefTypeName)

    lazy val withoutProduct: Seq[MethodInfo] = without(productName)

    lazy val withoutSerial: Seq[MethodInfo] =
      without(scalaSerializableName).
      filterNot(m => javaSerializableName.exists(_ == m.symbol.owner.fullName))
  }
}