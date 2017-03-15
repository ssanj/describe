## Features ##

Should provide quick access to the following for a type:

1. methods
  1. Allow searching by name, param types, flags and return types [x]
  1. Include implicit conversion methods? [x]
1. constructors [x]
  1. Allow searching by param types [x]
1. Classes [x]
1. baseclasses [x]
1. subclasses
1. implicits (across the companion as well) [x]
1. Final type
1. Alias (if any)
1. Modules [x]
1. Vals [x]
1. Vars [x]
1. Methods including those from implicit conversions [x]

----

1. List members of a package. (works only with preloaded packages like scala. Use a library to get this functionality - classutils)
2. Find package of a type (http://stackoverflow.com/questions/21428795/scala-reflection-derive-package-name) [x]
3. reflective find a type that satisfies a relationship within a hierarchy/package (http://stackoverflow.com/questions/18247002/scala-reflection-search)
4. Given a method name, return the class in which it is defined/overridden
5. Find instances of a type within a package. Eg. JsonDecoder[_] within argonaut

----

Issues:

1. Some methods that return generic types return a type 'T' when the methods take in a different letter such as 'A':

members[scala.math.Ordering.type].methodsByName("ordered").methodSignature.nl.print

gives:
    def ordered[A](evidence$1: A => Comparable[A]): Ordering[T]

This seems to be a reflection API issue. If we use Type.toString we get the correct Answer. This might be a workaround we need. [x]

2. All implicit does not return implicit instances, only implicit classes and methods. [x]

Features:

1. Add a ClassSignature class for printing out classes [x]

2. Add a ModuleSignature class for printing out modules [x]

3. Have a default profile to reduce verbosity of dsl:

    methods[T].defaults -> should be the same as:
    methods[T].methodSignature.sortAlpha.num.print

4. Add methodName Transform implicit. (MethodNameOps)

5. Add the ability to list members of a module. [x]

6. Add the ability to list members of a class. [x]

7. Rename <init> -> The name of the class. [x]

8. Add deprecated modifier to classes, modules and methods. [x]

9. Add .summary format to display a summary of a type:
    1. methods
    2. classes
    3. modules

    Similar to .info but more usable.

10. Return implicit instances available.
    It seems like implicit instances are returned as implicit methods. [x]

11. List methods of type such as Option should take into account any implicit conversions in:
 1. companion object [x]
 2. scala.Predef.

12. Add a 'rType' and 'symbol' variables to each type and symbol respectively.

13. Add a ValSignature class for printing out Vals. [x]

14. Add a VarSignature class for printing out Vars. [x]

15. All type-like classes should have all type-related functionality. (methods, classes, superclasses, subclasses) [x]

16. MethodSignature should specify whether it is an implicit. [x]

17. When listing MemberInfo signify which are objects and which are classes if possible. [x]

18. Make all package-level methods consistent: same parameters, same parameter groups. [x]

19. Support manifest entries. [not necessary as we get the cp from the REPL]

20. Support OSGI? [not necessary as we get the cp from the REPL]

21. Add without syntax to MemberInfo

22. Allow methods: methodsReturning, methodsWithParam etc across a Seq[MethodInfo]

23. Filter out $anon? https://github.com/scala/scala/blob/246653f024c13ba0348fec3f83b147de11251fe3/src/reflect/scala/reflect/internal/StdNames.scala [x]

24. Simple dsl for package paths:

case class p(value: String)
case class PackagePath(paths: p*) {
    def *(value: p): PackagePath = PackagePath((paths ++ Array(value)):_*)
}

implicit def pToPackagePath(value: p): PackagePath = PackagePath(value)

implicit def packagePathToReg(pp: PackagePath): scala.util.matching.Regex =
    (pp.paths.map(_.value).mkString("\\.") + "\\.").r

implicit def pToReg(path: p): scala.util.matching.Regex =
    (path.value + "\\.").r

    usage:
      getPackageClasses(cp, p("scala") * p("io"), false)
      getPackageClasses(cp, p("argonaut"), false)

25. Shorten package methods with sensible defaults: [x]

  def pkgCls(cp: Seq[java.io.File] = classPath,verbose: Boolean = false, pkg: scala.util.matching.Regex) = getPackageClasses(cp, pkg, verbose)

  usage:
    pkgCls(pkg = p("scala") * p("io")).d1

26. Return a log of the output and errors from package* methods.

27. Add try/catch around looking up members. [x]

28. Which class has a variable named?
    pkgClsWith(classPath, "scala\\.tools\\.nsc\\.".r)(Val, _.contains("classPath")) [x]

29. Replace Try with try/catch Linkage and others
- api.members.getPackageSubclasses[scala.tools.nsc.backend.jvm.opt.ByteCodeRepository](classPath, "scala\\.tools\\.nsc\\.".r, false) [x]

30. Cache package classes. (The filters keep changing so it might be wasteful)

31. Simplify (classPath, reg, verbose) usage. Allow defining once and reusing. [x]

 val ps = PackageSelector(classPath, "scala\\.tools\\.nsc\\.".r, false)

 getPackageSubclasses[scala.tools.nsc.backend.jvm.opt.ByteCodeRepository](ps)

 getPackageVals(ps)

 32. Allow transforming on a Seq[PackageSelect[A]] with a A => B [x]

 33. Add a summarize method which will write out a summary of a type:
  - superclasses [x]
  - constructors [x]
  - extractors [x]
  - methods [x]
  - vals [x]
  - vars [x]
  - subclasses [x]
  - Related classes (unique classes either passed in as parameters or returned as return types)
  - Add counts to the top of the headings:
    eg. Methods (36):

34. Add support to find typeClass instances.
  - implicits methods for the type
  - implicit vals for the type
  * This should be covered by implicits

35. Add pkgExtractors [x]

36. Add [extractor] marker to methods [x]

37. Remove object "constructor" from pkgConstructors list [x]

38. Remove object "constructor" from method list

39. Add apply methods of ModuleClasses to "constructor" list [x]

40. Add Show instance for types only (leave out param names).

  Eg. map => map[A,B](A => B): B

41. Show subclasses for a type [x]
    - get superclasses for each type
    - check for <:< behaviour

42. Release version once we have a stable feature-set

43. Remove dupes for methods_x

44. Add method equality check for the following:
    - name
    - type params
    - params (name and type)
    - return type
    - visibility (???)

45. methodsByParam2 also returns matches for a single type match:

  members[List[_]].methodsByParam2[Int, Int] =>
    1. def apply(n: Int): A
    2. def sliceWithKnownBound(from: Int, until: Int): Repr

46. methodsByParam2 does not work with Functionx types. How do we match against:
  def indexWhere(p: A => Boolean, from: Int): Int

47 methodsReturningX should use <:< instead of =:= [x]