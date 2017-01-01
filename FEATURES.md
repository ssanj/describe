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

1. List members of a package.
2. Find package of a type (http://stackoverflow.com/questions/21428795/scala-reflection-derive-package-name)
3. reflective find a type that satisfies a relationship within a hierarchy/package (http://stackoverflow.com/questions/18247002/scala-reflection-search)
4. Given a method name, return the class in which it is defined/overridden

----

Issues:

1. Some methods that return generic types return a type 'T' when the methods take in a different letter such as 'A':

members[scala.math.Ordering.type].methodsByName("ordered").methodSignature.nl.print

gives:
    def ordered[A](evidence$1: A => Comparable[A]): Ordering[T]

This seems to be a reflection API issue. If we use Type.toString we get the correct Answer. This might be a workaround we need.

2. All implicit does not return implicit instances, only implicit classes and methods.

Features:

1. Add a ClassSignature class for printing out classes

2. Have a default profile to reduce verbosity of dsl:

    methods[T].defaults -> should be the same as:
    methods[T].methodSignature.sortAlpha.num.print

3. Add methodName Transform implicit. (MethodNameOps)