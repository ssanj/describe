# Describe #

A Scala reflection utility to quickly list details of a type.

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

## Reflection ##

## methods ##

To list methods of a type use:

```
methods[Type].<mapping>.<sorting>.<formatting>.<output>
```

For example to list the methods on Option:

```
methods[Option[_]].methodSignature.sortAlpha.nl.print
```

which yields:

```
def !=(x$1: Any): Boolean
def ##: Int
def $asInstanceOf[T0]: T0
def $init$: Unit
def $isInstanceOf[T0]: Boolean
def <init>: Option[A]
def ==(x$1: Any): Boolean
def asInstanceOf[T0]: T0
def canEqual(that: Any): Boolean
def clone: Object
def collect[B](pf: PartialFunction[A,B]): Option[A]
def contains[A1 >: A](elem: A1): Boolean
def eq(x$1: AnyRef): Boolean
def equals(x$1: Any): Boolean
def exists(p: A => Boolean): Boolean
def filter(p: A => Boolean): Option[A]
def filterNot(p: A => Boolean): Option[A]
def finalize: Unit
def flatMap[B](f: A => Option[B]): Option[A]
def flatten[B](ev: <:<[A,Option[B]]): Option[A]
def fold[B](ifEmpty: => B)(f: A => B): B
def forall(p: A => Boolean): Boolean
def foreach[U](f: A => U): Unit
def get: A
def getClass: Class[T]
def getOrElse[B >: A](default: => B): B
def hashCode: Int
def isDefined: Boolean
def isEmpty: Boolean
def isInstanceOf[T0]: Boolean
def iterator: Iterator[A]
def map[B](f: A => B): Option[A]
def ne(x$1: AnyRef): Boolean
def nonEmpty: Boolean
def notify: Unit
def notifyAll: Unit
def orElse[B >: A](alternative: => Option[B]): Option[A]
def orNull[A1 >: A](ev: <:<[Null,A1]): A1
def productArity: Int
def productElement(n: Int): Any
def productIterator: Iterator[A]
def productPrefix: String
def synchronized[T0](x$1: T0): T0
def toLeft[X](right: => X): <refinement>
def toList: List[A]
def toRight[X](left: => X): <refinement>
def toString: String
def wait: Unit
def wait(x$1: Long): Unit
def wait(x$1: Long, x$2: Int): Unit
def withFilter(p: A => Boolean): WithFilter
```

Lets look at some of the supplied arguments in turn.

__Mapping__: Specifies the type to which the results should be mapped to.
In the above example we use __methodSignature__ which maps the results
to a [MethodSignature](https://github.com/ssanj/describe/blob/master/src/main/scala/net/ssanj/describe/api/MethodSignature.scala). Additional mappings can be
specified via the __transform__ function:

```
methods[Option[_]].transform(_.name).nl.print
```

which simply prints out the name of each method:

```
toLeft
toRight
toList
iterator
orElse
collect
foreach
forall
exists
contains
withFilter
nonEmpty
filterNot
filter
flatten
flatMap
fold
map
orNull
getOrElse
isDefined
<init>
productPrefix
productIterator
$init$
$asInstanceOf
$isInstanceOf
synchronized
##
!=
==
ne
eq
notifyAll
notify
clone
getClass
hashCode
toString
equals
wait
wait
wait
finalize
asInstanceOf
isInstanceOf
get
isEmpty
productArity
productElement
canEqual
```

__Sorting__: The way in which to sort the results. In the example about we sort [MethodSignature](https://github.com/ssanj/describe/blob/master/src/main/scala/net/ssanj/describe/api/MethodSignature.scala) alphabetically via __sortAlpha__. To sort any custom type bring an implicit __scala.math.Ordering__ instance for that type into scope and call __sortAlpha__:

```
def sortAlpha(implicit ord: Ordering[T]): Seq[T]
```

An example usage of using __sortAlpha__ with a custom Ordering:

```
methods[Option[_]].transform(_.name).sortAlpha.nl.print
```

The above works because we have an implicit __scala.math.Ordering[String]__ instance in scope from scala.math.Ordering.

To reverse an Ordering, simply call __reverse__:

```
methods[Option[_]].transform(_.name).sortAlpha.reverse.nl.print
```

If you want to specify a custom sorting without an implicit Ordering, use the __sort__ method:

```
def sort(isBefore: (T, T) => Boolean): Seq[T]
```

For example to use __sort__ for sorting by the number of parameters of a method:

```
methods[java.util.Comparator[_]].methodSignature.sort { case (m1,m2) => m1.paramLists.flatten.length < m2.paramLists.flatten.length }.nl.print
```

Which results in:

```
def <init>: Comparator[T]
def reversed: Comparator[T]
def $asInstanceOf[T0]: T0
def $isInstanceOf[T0]: Boolean
def ##: Int
def notifyAll: Unit
def notify: Unit
def clone: Object
def getClass: Class[T]
def hashCode: Int
def toString: String
def wait: Unit
def finalize: Unit
def asInstanceOf[T0]: T0
def isInstanceOf[T0]: Boolean
def thenComparingDouble(x$1: java.util.function.ToDoubleFunction[_ >: T]): Comparator[T]
def thenComparingLong(x$1: java.util.function.ToLongFunction[_ >: T]): Comparator[T]
def thenComparingInt(x$1: java.util.function.ToIntFunction[_ >: T]): Comparator[T]
def thenComparing(x$1: java.util.Comparator[_ >: T]): Comparator[T]
def thenComparing[U <: java.lang.Comparable[_ >: U]](x$1: java.util.function.Function[_ >: T, _ <: U]): Comparator[T]
def synchronized[T0](x$1: T0): T0
def !=(x$1: Any): Boolean
def ==(x$1: Any): Boolean
def ne(x$1: AnyRef): Boolean
def eq(x$1: AnyRef): Boolean
def equals(x$1: Any): Boolean
def wait(x$1: Long): Unit
def thenComparing[U](x$1: java.util.function.Function[_ >: T, _ <: U], x$2: java.util.Comparator[_ >: U]): Comparator[T]
def wait(x$1: Long, x$2: Int): Unit
def compare(x$1: T, x$2: T): Int
def lambda$thenComparing$36697e65$1(x$1: java.util.Comparator[_], x$2: Any, x$3: Any): Int
```

__Formatting__: Specifies how to format the results. Formatting produces a String from matched results. The following default formats are provided:
 1. __nl__  - Results separated by a newline.
 1. __num__ - Results separated by a newline with each given a consecutive number.

To specify your own formatting for a type provide an implicit instance of [Format](https://github.com/ssanj/describe/blob/master/src/main/scala/net/ssanj/describe/api/Format.scala) and [Show](https://github.com/ssanj/describe/blob/master/src/main/scala/net/ssanj/describe/api/Show.scala) in scope.

__OUTPUT__: Specifies where to write the formatted output to. The default output provided is the __print__ which writes the formatted String to stdout. To add custom output types create an implicit instance of [Print](https://github.com/ssanj/describe/blob/master/src/main/scala/net/ssanj/describe/api/Print.scala) in scope.

## classes ##

To list classes of a type use:

```
classes[Type]
```

For example to list the classes on Option:

```
classes[Option[_]]
```

which yields a Seq of __ClassInfo__ instances:

```
res66: Seq[net.ssanj.describe.api.ClassInfo] = List(ClassInfo(class WithFilter))
```