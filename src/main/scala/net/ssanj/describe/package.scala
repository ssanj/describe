package net.ssanj

import scala.reflect.runtime.universe.TypeTag
import scala.language.implicitConversions
import java.io.File

package object describe {

  import scala.tools.nsc.interpreter.{StdReplVals, ISettings}
  import api.{Defaults, Format, Show, Sorted, Print}
  import scala.tools.nsc.util.ClassFileLookup
  import scala.tools.nsc.io.AbstractFile


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

  def methodsX[T: TypeTag] = api.members.info[T].methodsX

  def methods[T: TypeTag](value: T) = api.members.info[T](value).methods

  def members[T: TypeTag] = api.members.info[T]

  def members[T: TypeTag](value: T) = api.members.info[T](value)

  import scala.tools.nsc.util.ClassFileLookup
  import scala.tools.nsc.io.AbstractFile

  def findInstances[T: TypeTag](classpath: ClassFileLookup[AbstractFile], p: String => Boolean) = api.members.findInstances[T](classpath, p)

  def declaredOn[T: TypeTag] = api.members.info[T].declared

  def declaredOn[T: TypeTag](value: T) = api.members.info[T](value).declared

  def vars[T: TypeTag]    = api.members.info[T].vars

  def vals[T: TypeTag]    = api.members.info[T].vals

  def classes[T: TypeTag] = api.members.info[T].classes

  def modules[T: TypeTag] = api.members.info[T].modules

  //access to the reflection universe
  lazy val ru = scala.reflect.runtime.universe

  //access to the current mirror
  lazy val cm = scala.reflect.runtime.currentMirror

  lazy val anyMethods = declaredOn[Any]

  lazy val anyRefMethods = declaredOn[AnyRef]

  lazy val productMethods = declaredOn[Product]

  lazy val serializableMethods = declaredOn[Serializable] ++ declaredOn[java.io.Serializable]

  def shortNames = api.Transform.shortNames(_)

  def getPackage[T: TypeTag] = api.getPackage[T]

  implicit def toCp = api.members.toCp _

  def getPackageClasses(classpath: Seq[File], packageFilter: scala.util.matching.Regex,
    verbose: Boolean = false) = api.members.getPackageClasses(classpath, packageFilter, verbose)

  def findInstances1[T: TypeTag](classpath: ClassFileLookup[AbstractFile], p: String => Boolean) =
    api.members.findInstances[T](classpath, p)(implicitly[TypeTag[T]])

  def findInstances2[T: TypeTag](classpath: Seq[File], packageFilter: scala.util.matching.Regex,
    verbose: Boolean = false) =
      api.members.findInstances2[T](classpath, packageFilter, verbose)(implicitly[TypeTag[T]])

  implicit def imSeqToFormat[T: Show](values: Seq[T]): Format[T] = new Format[T](values, implicitly[Show[T]])

  implicit def imSeqToDefaults[T: Show : Ordering](values: Seq[T]): Defaults[T] = new Defaults[T](values)

  implicit def imOrderingOfSeq[A : Ordering]: Ordering[Seq[A]] = new Ordering[Seq[A]] {
    override def compare(t1: Seq[A], t2: Seq[A]): Int = {
      t1.length - t2.length
    }
  }

  implicit def imSeqToSorted[T](values: Seq[T]): Sorted[T] = new Sorted[T](values)

  implicit def describeToReflectType(t: ru.Type): scala.reflect.runtime.universe.Type = t.asInstanceOf[scala.reflect.runtime.universe.Type]

  implicit def stringtToPrint(value: String): Print = new Print(value)
}

