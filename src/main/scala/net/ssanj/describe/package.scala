package net.ssanj

import scala.reflect.runtime.universe.TypeTag
import scala.language.implicitConversions

package object describe {

  import scala.tools.nsc.interpreter.{StdReplVals, ISettings}
  import api.{Format, Show, Print}

  /** Sets the maximum output line length (in characters) for any repl output.
    * This is done via the '''maxPrintString''' field of the ISettings
    * variable for a given repl '''vals''' in power mode.
    *
    *  @example {{{
    *    replMaxPrint(vals, 5000)
    *  }}}
    *
    * @param vals The '''vals''' variable when in power mode.
    * @param length The number of characters to increase the output to.
    * The default is 800 characters.
    *
    * @see [[scala.tools.nsc.interpreter.StdReplVals]]
    * @see [[scala.tools.nsc.interpreter.ISettings]]
    */
  def replMaxPrint(vals: StdReplVals, length: Int): Unit = {
    vals.isettings.maxPrintString = length
  }

  /** Returns the current settings defined on the repl '''vals''' when in power mode.
    *
    *  @example {{{
    *    replSettings(vals)
    *  }}}
    *
    * @param vals The '''vals''' variable when in power mode.
    * @see [[scala.tools.nsc.interpreter.StdReplVals]]
    * @see [[scala.tools.nsc.interpreter.ISettings]]
    */
  def replSettings(vals: StdReplVals): ISettings = {
    vals.isettings
  }

  def methods[T: TypeTag] = api.members.info[T].methods

  def methods[T: TypeTag](value: T) = api.members.info[T](value).methods

  def members[T: TypeTag] = api.members.info[T]

  def members[T: TypeTag](value: T) = api.members.info[T](value)

  def vars[T: TypeTag]    = api.members.info[T].vars

  def vals[T: TypeTag]    = api.members.info[T].vals

  def classes[T: TypeTag] = api.members.info[T].classes

  //access to the reflection universe
  lazy val ru = scala.reflect.runtime.universe

  //access to the current mirror
  lazy val cm = scala.reflect.runtime.currentMirror

  implicit def imSeqToFormat[T: Show](values: Seq[T]): Format[T] = new Format[T](values, implicitly[Show[T]])

  implicit def imStringToPrint(value: String): Print = new Print(value)

  implicit def describeToReflectType(t: ru.Type): scala.reflect.runtime.universe.Type = t.asInstanceOf[scala.reflect.runtime.universe.Type]
}

