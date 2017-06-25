package net.ssanj.describe.api

class NoCompanionObject
object NoCompanionClass

class Animal {
  var happiness = 0

  val roar: String = "Growl"

  val getName: String = "Snappy"

  def pet(pats: Int): Int = {
    happiness += pats
    happiness
  }
}
