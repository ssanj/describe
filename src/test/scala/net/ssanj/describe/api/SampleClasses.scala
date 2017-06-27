package net.ssanj.describe.api

class NoCompanionObject
object NoCompanionClass

class Animal (base: Int, name: String) {
  var happiness = base

  val roar: String = "Growl"

  val getName: String = name

  def pet(pats: Int): Int = {
    happiness += pats
    happiness
  }
}
