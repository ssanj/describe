## Features ##

Should provide quick access to the following for a type:

1. methods
  1. Allow searching by name, param types, flags and return types
  1. Include implicit conversion methods?
1. constructors
  1. Allow searching by param types
1. classes
1. baseclasses [x]
1. subclasses
1. implicits (across the companion as well) [x]
1. Final type
1. Alias (if any)

----

1. List members of a package. (works only with preloaded packages like scala. Use a library to get this functionality)
2. Find package of a type (http://stackoverflow.com/questions/21428795/scala-reflection-derive-package-name)
3. reflective find a type that satisfies a relationship within a hierarchy/package (http://stackoverflow.com/questions/18247002/scala-reflection-search)
4. Given a method name, return the class in which it is defined/overridden

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