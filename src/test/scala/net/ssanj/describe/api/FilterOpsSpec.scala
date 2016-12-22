package net.ssanj.describe
package api

import org.scalatest.{Matchers, WordSpecLike}

final class FilterOpsSpec extends Matchers with WordSpecLike {

  private val anyMethods = methods[Any]

  "Filter Operations" should {
    "not return any methods" when {
      "withoutAny is called on members of Any" in {
        anyMethods should not be ('empty)
        anyMethods.withoutAny should be ('empty)
      }
    }
  }

  it should {
    "not return methods of Any" when {
      "withoutAny is called on returned methods" in {
        val stringMethods = methods[String]
        stringMethods should not be ('empty)
        val stringMethodsWithoutAny = stringMethods.withoutAny
        stringMethods.filterNot(anyMethods.contains) should be (stringMethodsWithoutAny)
      }
    }
  }
}