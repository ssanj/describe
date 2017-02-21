package net.ssanj

import scala.reflect.runtime.universe.TypeTag
import scala.language.implicitConversions

package object describe {

  import scala.tools.nsc.interpreter.{StdReplVals, ISettings}
  import api.{Default, Defaults, Format, Show, Sorted, Print}

  private[describe] val newLine = System.lineSeparator


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

  def methods_+[T: TypeTag] = api.members.info[T].methodsX

  def methods[T: TypeTag](value: T) = api.members.info[T](value).methods

  def members[T: TypeTag] = api.members.info[T]

  def members[T: TypeTag](value: T) = api.members.info[T](value)

  def declaredOn[T: TypeTag] = api.members.info[T].declared

  def declaredOn[T: TypeTag](value: T) = api.members.info[T](value).declared

  def vars[T: TypeTag]    = api.members.info[T].vars

  def vals[T: TypeTag]    = api.members.info[T].vals

  def classes[T: TypeTag] = api.members.info[T].classes

  def modules[T: TypeTag] = api.members.info[T].modules

  def constructors[T: TypeTag] = api.members.info[T].constructors

  def constructors[T: TypeTag](value: T) = api.members.info[T].constructors

  def extractors[T: TypeTag] = api.members.info[T].extractors

  def extractors[T: TypeTag](value: T) = api.members.info[T].extractors

  def implicits[T: TypeTag] = api.members.info[T].methods.filter(_.isImplicit)

  def implicits[T: TypeTag](value: T) = api.members.info[T].methods.filter(_.isImplicit)

  //access to the reflection universe
  lazy val ru = scala.reflect.runtime.universe

  //access to the current mirror
  lazy val cm = scala.reflect.runtime.currentMirror

  lazy val anyMethods = declaredOn[Any]

  lazy val anyRefMethods = declaredOn[AnyRef]

  lazy val productMethods = declaredOn[Product]

  lazy val serializableMethods = declaredOn[Serializable] ++ declaredOn[java.io.Serializable]

  def shortNames = api.Transform.shortNames(_)

  implicit def toCp = api.members.toCp _

  lazy val PackageSelect = api.members.PackageSelect

  type PackageSelect = api.members.PackageSelect

  def pkgClasses(implicit ps: api.members.PackageSelect) =
    api.members.getPackageClasses(ps)

  def pkgImplicits(implicit ps: api.members.PackageSelect) =
    api.members.getPackageImplicits(ps)

  def pkgVals(implicit ps: api.members.PackageSelect) =
    api.members.getPackageVals(ps)

  def pkgVars(implicit ps: api.members.PackageSelect) =
    api.members.getPackageVars(ps)

  //pkgSubclasses[T](implicitly, ps)
  def pkgSubclasses[T: TypeTag](implicit ps: api.members.PackageSelect) =
    api.members.getPackageSubclasses[T](ps)

  def pkgExtractors(implicit ps: api.members.PackageSelect) =
    api.members.getPackageExtractors(ps)

  def pkgConstructors(implicit ps: api.members.PackageSelect) =
    api.members.getPackageConstructors(ps)

  def pkgModules(implicit ps: api.members.PackageSelect) =
    api.members.getPackageModules(ps)

  def pkgAbstractClasses(implicit ps: api.members.PackageSelect) =
    api.members.getPackageAbstractClasses(ps)

  def pkgTraits(implicit ps: api.members.PackageSelect) =
    api.members.getPackageTraits(ps)

  def pkgVals_?(f: api.ValInfo => Boolean)(implicit ps: api.members.PackageSelect) =
    api.members.findPackageVals(f)(ps)

  def pkgMethods_?(f: api.MethodInfo => Boolean)(implicit ps: api.members.PackageSelect) =
    api.members.findPackageMethods(f)(ps)

  def pkg_*[T](f: api.MemberInfo => Seq[T])(implicit ps: api.members.PackageSelect) =
    api.members.getPackageAnything[T](f)(ps)

  def summarise[T: TypeTag] = api.members.summarise[T]

  implicit def imSeqToFormat[T: Show](values: Seq[T]): Format[T] = new Format[T](values, implicitly[Show[T]])

  implicit def imSeqToDefaults[T: Show : Ordering](values: Seq[T]): Defaults[T] = new Defaults[T](values)

  implicit def instanceToDefaults[T: Show](value: T): Default[T] = new Default[T](value)

  implicit def imSeqToSorted[T](values: Seq[T]): Sorted[T] = new Sorted[T](values)

  implicit def describeToReflectType(t: ru.Type): scala.reflect.runtime.universe.Type = t.asInstanceOf[scala.reflect.runtime.universe.Type]

  implicit def stringtToPrint(value: String): Print = new Print(value)

  implicit def packageElementToTransform[T](values: api.PackageElement[T]):
    api.Transform[api.PackageElement, T] = new api.Transform[api.PackageElement, T](values)

  implicit def seqPackageElementToTransform[T](values: Seq[api.PackageElement[T]]):
    api.Transform[({type X[Z]=Seq[api.PackageElement[Z]]})#X, T] = new api.Transform[({type X[Z]=Seq[api.PackageElement[Z]]})#X, T](values)

  implicit def nestedFunctor[S[_], T[_]](implicit FS: api.Functor[S], FT: api.Functor[T]): api.Functor[({type X[Z]=S[T[Z]]})#X] = new api.Functor[({type X[Z]=S[T[Z]]})#X] {
    def map[A, B](fa: S[T[A]], f: A => B): S[T[B]] = FS.map(fa, FT.map(_: T[A], f))
  }

  implicit val seqFunctor: api.Functor[Seq] = new api.Functor[Seq] {
    def map[A, B](fa: Seq[A], f: A => B): Seq[B] = fa map f
  }
}

