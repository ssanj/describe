# Describe #

A Scala reflection utility to quickly list details of a type.

## Discovery ##

If you're like me, you use the Scala repl to discover an API. It might also have something to do with not wanting to use an IDE ;)

How would you go about learning a new API with just the Scala repl? You have  tab completion which you can use to list all the methods of a given instance:

```
import scala.util.Try
val t = Try(1)
t.<tab>
```

which yields:

```
WithFilter   flatMap   get         isSuccess   recover       transform
failed       flatten   getOrElse   map         recoverWith   withFilter
filter       foreach   isFailure   orElse      toOption
```

You could also complete a method name with its signature if you know at least part of its name:

```
t.ma<tab>
```

yields:

```
t.map
```

and further tabbing, yields the full method signature for the method:

```
def map[U](f: Int => U): scala.util.Try[U]
```

What if you wanted more?

Once we import the describe api:

```
import net.ssanj.describe._
```

we are ready to start having some fun! :)

### Methods ###

Using __methods__ you can easily list all the methods on any type in one go:

```
methods[scala.util.Try[_]]
```

_We use a type constructor here_ (```Try[_]```) _because the scala.util.Try type takes in a type argument. Notice how we did not need to create an instance to get this information as in the previous example_.

which yields a Seq of api.MethodInfo objects:

```
res11: Seq[net.ssanj.describe.api.MethodInfo] = List(MethodInfo(method transform), MethodInfo(method toOption), MethodInfo(method withFilter), MethodInfo(method orElse), MethodInfo(method getOrElse), MethodInfo(constructor Try), MethodInfo(method $asInstanceOf), MethodInfo(method $isInstanceOf), MethodInfo(method synchronized), MethodInfo(method ##), MethodInfo(method !=), MethodInfo(method ==), MethodInfo(method ne), MethodInfo(method eq), MethodInfo(method notifyAll), MethodInfo(method notify), MethodInfo(method clone), MethodInfo(method getClass), MethodInfo(method hashCode), MethodInfo(method toString), MethodInfo(method equals), MethodInfo(method wait), MethodInfo(method wait), MethodInfo(method wait), MethodInfo(method finalize), MethodInfo(method asInstanceOf), MethodInfo(method ...
```

To format the result into a readable output use one of the __d__ methods:

```
res11.d1
```

which yields:

```
 1. def !=(x$1: Any): Boolean
 2. def ##: Int
 3. def $asInstanceOf[T0]: T0
 4. def $isInstanceOf[T0]: Boolean
 5. def ==(x$1: Any): Boolean
 6. [constructor] def Try: scala.util.Try[T]
 7. def asInstanceOf[T0]: T0
 8. def clone: java.lang.Object
 9. def eq(x$1: AnyRef): Boolean
10. def equals(x$1: Any): Boolean
11. def failed: scala.util.Try[Throwable]
12. def filter(p: T => Boolean): scala.util.Try[T]
13. def finalize: Unit
14. def flatMap[U](f: T => scala.util.Try[U]): scala.util.Try[U]
15. def flatten[U](ev: <:<[T,scala.util.Try[U]]): scala.util.Try[U]
16. def foreach[U](f: T => U): Unit
17. def get: T
18. def getClass: java.lang.Class[_]
19. def getOrElse[U >: T](default: => U): U
20. def hashCode: Int
21. def isFailure: Boolean
22. def isInstanceOf[T0]: Boolean
23. def isSuccess: Boolean
24. def map[U](f: T => U): scala.util.Try[U]
25. def ne(x$1: AnyRef): Boolean
26. def notify: Unit
27. def notifyAll: Unit
28. def orElse[U >: T](default: => scala.util.Try[U]): scala.util.Try[U]
29. def recover[U >: T](f: PartialFunction[Throwable,U]): scala.util.Try[U]
30. def recoverWith[U >: T](f: PartialFunction[Throwable,scala.util.Try[U]]): scala.util.Try[U]
31. def synchronized[T0](x$1: T0): T0
32. def toOption: Option[T]
33. def toString: java.lang.String
34. def transform[U](s: T => scala.util.Try[U], f: Throwable => scala.util.Try[U]): scala.util.Try[U]
35. def wait: Unit
36. def wait(x$1: Long): Unit
37. def wait(x$1: Long, x$2: Int): Unit
38. def withFilter(p: T => Boolean): Try.this.WithFilter
```

The ```d1``` method is just the first default formatting which sorts the results into separate numbered lines. There is also a ```d2``` that sorts the results into lines without numbering. The general rule is that if you want to use describe as an API, you can directly manipulate api.MethodInfo. Alternatively if you just want display some results to STDOUT, use the __d__ methods or write your own formatting. More on that later.

If you did have an instance of a type you could achieve the same result with:

```
methods(t).d1
```

If we want list the methods of a specific subclass of a type we can directly just use that type with __methods__:

```
scala> methods[scala.util.Success[_]].d1

 1. def !=(x$1: Any): Boolean
 2. def ##: Int
 3. def $asInstanceOf[T0]: T0
 4. def $init$: Unit
 5. def $isInstanceOf[T0]: Boolean
 6. def ==(x$1: Any): Boolean
 7. [constructor] def Success(value: T): scala.util.Success[T]
 8. def asInstanceOf[T0]: T0
 9. def canEqual(x$1: Any): Boolean
10. def clone: java.lang.Object
11. def copy[T](value: T): scala.util.Success[T]
12. def copy$default$1[T]: T @scala.annotation.unchecked.uncheckedVariance
13. def eq(x$1: AnyRef): Boolean
14. def equals(x$1: Any): Boolean
15. def failed: scala.util.Try[Throwable]
16. def filter(p: T => Boolean): scala.util.Try[T]
17. def finalize: Unit
18. def flatMap[U](f: T => scala.util.Try[U]): scala.util.Try[U]
19. def flatten[U](ev: <:<[T,scala.util.Try[U]]): scala.util.Try[U]
20. def foreach[U](f: T => U): Unit
21. def get: T
22. def getClass: java.lang.Class[_]
23. def getOrElse[U >: T](default: => U): U
24. def hashCode: Int
25. def isFailure: Boolean
26. def isInstanceOf[T0]: Boolean
27. def isSuccess: Boolean
28. def map[U](f: T => U): scala.util.Try[U]
29. def ne(x$1: AnyRef): Boolean
30. def notify: Unit
31. def notifyAll: Unit
32. def orElse[U >: T](default: => scala.util.Try[U]): scala.util.Try[U]
33. def productArity: Int
34. def productElement(x$1: Int): Any
35. def productIterator: Iterator[Any]
36. def productPrefix: String
37. def recover[U >: T](rescueException: PartialFunction[Throwable,U]): scala.util.Try[U]
38. def recoverWith[U >: T](f: PartialFunction[Throwable,scala.util.Try[U]]): scala.util.Try[U]
39. def synchronized[T0](x$1: T0): T0
40. def toOption: Option[T]
41. def toString: String
42. def transform[U](s: T => scala.util.Try[U], f: Throwable => scala.util.Try[U]): scala.util.Try[U]
43. def value: T
44. def wait: Unit
45. def wait(x$1: Long): Unit
46. def wait(x$1: Long, x$2: Int): Unit
47. def withFilter(p: T => Boolean): Try.this.WithFilter
```

Now you might notice there are a lot of uninteresting methods like those from scala.Any, scala.AnyRef and scala.Product etc. You can filter out many of these methods, if they have not been overridden in the target type, by chaining together the following __without__ methods:

1. withoutAny (without scala.Any methods)
1. withoutAnyRef (without scala.AnyRef methods)
1. withoutProduct (without scala.Product methods)
1. withoutSerial (without scala.Serializable and java.io.Serialiable methods)

For example to filter out methods from scala.AnyRef from ```scala.util.Success``` do:

```
methods[scala.util.Success[_]].withoutAnyRef.d1
```

which yields:

```
 1. def $init$: Unit
 2. [constructor] def Success(value: T): scala.util.Success[T]
 3. def asInstanceOf[T0]: T0
 4. def canEqual(x$1: Any): Boolean
 5. def copy[T](value: T): scala.util.Success[T]
 6. def copy$default$1[T]: T @scala.annotation.unchecked.uncheckedVariance
 7. def equals(x$1: Any): Boolean
 8. def failed: scala.util.Try[Throwable]
 9. def filter(p: T => Boolean): scala.util.Try[T]
10. def flatMap[U](f: T => scala.util.Try[U]): scala.util.Try[U]
11. def flatten[U](ev: <:<[T,scala.util.Try[U]]): scala.util.Try[U]
12. def foreach[U](f: T => U): Unit
13. def get: T
14. def getOrElse[U >: T](default: => U): U
15. def hashCode: Int
16. def isFailure: Boolean
17. def isInstanceOf[T0]: Boolean
18. def isSuccess: Boolean
19. def map[U](f: T => U): scala.util.Try[U]
20. def orElse[U >: T](default: => scala.util.Try[U]): scala.util.Try[U]
21. def productArity: Int
22. def productElement(x$1: Int): Any
23. def productIterator: Iterator[Any]
24. def productPrefix: String
25. def recover[U >: T](rescueException: PartialFunction[Throwable,U]): scala.util.Try[U]
26. def recoverWith[U >: T](f: PartialFunction[Throwable,scala.util.Try[U]]): scala.util.Try[U]
27. def toOption: Option[T]
28. def toString: String
29. def transform[U](s: T => scala.util.Try[U], f: Throwable => scala.util.Try[U]): scala.util.Try[U]
30. def value: T
31. def withFilter(p: T => Boolean): Try.this.WithFilter
```

_Notice that we still get the equals and hashCode methods. This is because these methods have been overridden in the scala.util.Success case class_.

TODO: Should we have a way to filter out methods irrespective of whether they have been overridden?

If you only want to list the methods declared on a type, use __declaredOn__:

```
declaredOn[scala.util.Try[_]].d1
```

which yields:

```
 1. [constructor] def Try: scala.util.Try[T]
 2. def failed: scala.util.Try[Throwable]
 3. def filter(p: T => Boolean): scala.util.Try[T]
 4. def flatMap[U](f: T => scala.util.Try[U]): scala.util.Try[U]
 5. def flatten[U](ev: <:<[T,scala.util.Try[U]]): scala.util.Try[U]
 6. def foreach[U](f: T => U): Unit
 7. def get: T
 8. def getOrElse[U >: T](default: => U): U
 9. def isFailure: Boolean
10. def isSuccess: Boolean
11. def map[U](f: T => U): scala.util.Try[U]
12. def orElse[U >: T](default: => scala.util.Try[U]): scala.util.Try[U]
13. def recover[U >: T](f: PartialFunction[Throwable,U]): scala.util.Try[U]
14. def recoverWith[U >: T](f: PartialFunction[Throwable,scala.util.Try[U]]): scala.util.Try[U]
15. def toOption: Option[T]
16. def transform[U](s: T => scala.util.Try[U], f: Throwable => scala.util.Try[U]): scala.util.Try[U]
17. def withFilter(p: T => Boolean): Try.this.WithFilter
```

What if you wanted to see only the implicit methods defined within a type? You could use the __implicits__ method:

```
implicits[scala.Option.type].d1
```

which yields:

```
 1. implicit def option2Iterable[A](xo: Option[A]): Iterable[A]
```

_We use Option.type here to specify the class for the Option object (ModuleClass)._

### Members ###

TODO

## Repl ##

### Power Mode ###

These features are available in the power mode of the repl. You can enter power mode by entering:

```
:power
```

or

```
:po
```

which will then welcome you with:

```
** Power User mode enabled - BEEP WHIR GYVE **
** :phase has been set to 'typer'.          **
** scala.tools.nsc._ has been imported      **
** global._, definitions._ also imported    **
** Try  :help, :vals, power.<tab>           **
```

If you want to see the current values for power settings you can use __replSettings__:

```
replSettings(vals)
```

which will return the current repl settings:

```
ISettings {
  deprecation = false
  maxAutoprintCompletion = 250
  maxPrintString = 800
  unwrapStrings = true
}
```

Sometimes the repl truncates an output of a command, which can be annoying. If you want to change the output length allowed for commands then call __replMaxPrint__ with the desired length. For unlimited length use a value of __0__. If you want to set a max length, the maximum allowed constraint is Int.MaxValue.

```
replMaxPrint(vals, 5000)
```