How do we represent a symbol?
 One of x types?
 - ValInfo (TermSymbol)
 - VarInfo (TermSymbol)
 - MethodInfo (MethodSymbol)
 - ClassInfo (ClassSymbol)
 - TypeInfo (Type) (ParamInfo is subsumed into this)
 - AbstractTypeInfo (TypeSymbol)
 - ModuleInfo (ModuleSymbol)

How do we represent a Type?
- MemberInfo. Possibly rename to TypeInfo

What's the difference between a ClassSymbol and a Type?
 One's a symbol and the other a type. A ClassSymbol <: TypeSymbol.

How do we convert from a symbol to a Type?
symbol.asType.toType
symbol.typeSignature (for parameters)
symbol.info

How do we convert from a Type to a symbol?
type.typeSymbol or
type.termSymbol

What is a safe way to convert from Type to a TypeSymbol and TermSymbol.
Option[TypeInfo] (ClassInfo, Module Info), Option[TermInfo] (ValInfo, VarInfo, MethodInfo)

Do we need to override equals and hashCode to allow for proper comparisons? Eg. contains?

Should we only list public entities?

How does the "normal" equals matching working with Symbol? Does it change if we have different owners?

How to handle typeclasses in a context? Show for fullMethodNames, Show for name, show for Method definitions? Add context as a type param?
 - an easier was is to wrap each type in a "context" then define Shows for those.
 - given a Person => FullName(Person), FirstName(Person)

Show[Context, T]

What is the difference in the Repl between a String and writing to standout?
 Strings seem to be constrained by the length of vals.isettings.maxPrintString, while println does not.

How do we incorporate sorting into the query dsl?

What types are encapsulated in ParamInfo? Is it always ClassSymbol? TypeSymbol?

When should we return implicit instances? Do they count as 'methods'?

Do we need 'Transform'? Can we simply use map with helper methods
like: map(shortNames) or map(methodSignature).
 - Replace with map and custom methods.

Where do we use isPackageClass? For classes defined in a package object?

Do we need to return ModuleClasses when looking for classes? As in match on Module Classes or Modules and then get their classes?
 - No, these should be returned for module.

Which implicit conversions should we automatically add?
 - Ones from the companion object? [yes]
 - Predef. Maybe confusing. Eg. WrappedString vs StringOps

What does it mean to have a private val within a case class?

Why does None require an ascription (None: Option[T]) and none[T] does not?

How do we convert between a class utils class and a describe class?
 - load classes with class utils
 - get currentRuntimeMirror
 - cm.staticClass(className).toType.erasure
 * The library has to be on the classpath for this to work.

How do we get Classutils to read the correct classpath?

Why does getting fullName of the "scala." package fail with either Symbol errors, classpath errors or Match errors?

  - getPackageClasses(res1,"""scala.""".r).map(_.fullName)
    scala.MatchError: null

  - getPackageClasses(res1,"""scala.""".r).flatMap(mi => scala.util.Try(mi.fullName).getOrElse("~"))
    java.lang.NoClassDefFoundError: org/apache/tools/ant/taskdefs/MatchingTask
    Caused by: java.lang.ClassNotFoundException: org.apache.tools.ant.taskdefs.MatchingTask

  [x] Not using the correct classpath!

How do we skip over any erroneous classes that break the package loading?
 - Filter them out.
   Eg. scala package loading fails on a number of classes in tools, reflect and xml: getPackageClasses(classPath, "scala\\.(?!(reflect|tools|xml))".r)

How can we selectively change the type of symbol for Member from type to class?
 - No but we can get a Option[ClassInfo] from Member via 'asClass'

Can we retrieve the classPath programmaticly instead of needing to switch to power mode?

How can we see all implicits across a package?

Why does cm.staticModule("object") does not return implicit markers?

Eg. cm.staticModule("scala.Option$").moduleClass.asClass.toType.members.

 - This seems to be linked to how java named classes ("scala.concurrent.ExecutionContext$Implicits$") are loaded via cm.staticClass. There are a couple of ways around this.
  1. convert the java class name to a Scala one and use staticModule:
    //remove last "$" and replace all others by "."
    scala.concurrent.ExecutionContext$Implicits$ ->
    val nm = scala.concurrent.ExecutionContext.Implicits
    cm.staticModule(nm)
  2. Load the class explicitly and then call moduleSymbol:
    val cl = Class.forName("scala.concurrent.ExecutionContext$Implicits$")
    cm.moduleSymbol(cl)

Why are there duplicate classes listed in the packageClasses list?
 - was using allImplicits which looks in both the class and moduleClass.

Why are we seeing different results for Sublclasses on multiple runs?
 api.members.getPackageSubclasses[DecodeJson[_]](classPath, "argonaut\\.".r, true).d1

 First run:
  1. argonaut.DecodeJson [trait]
 Second run:
  1. argonaut.CodecJson [trait]
  1. argonaut.DecodeJson [trait]
 Other runs:
   1. argonaut.CodecJson [trait]
   2. argonaut.CodecJson$$anon$1 [class]
   3. argonaut.CodecJson$$anon$2 [class]
   4. argonaut.DecodeJson [trait]
   5. argonaut.DecodeJson$$anon$1 [class]
   6. argonaut.DecodeJson$$anon$2 [class]
   7. argonaut.DecodeJson$$anon$3 [class]
   8. argonaut.DecodeJson$$anon$4 [class]