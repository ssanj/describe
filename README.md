# Describe #

A Scala reflection utility to quickly list details of a type.

## Discovery ##

If you're like me, you use the Scala repl to discover an API. It might also have something to do with not wanting to use an IDE ;)

How would you go about learning a new API with just the Scala repl? You have  tab completion which you can use to list all the methods of a given instance:

```
scala> import scala.util.Try
scala> val t = Try(1)
t: scala.util.Try[Int] = Success(1)
scala> t.<tab>
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
methods[scala.util.Success[_]]
```

_We use a type constructor here_ (```Success[_]```) _because the scala.util.Success type takes in a type argument. Notice how we did not need to create an instance to get this information as in the previous example_.

which yields a Seq of api.MethodInfo objects:

```
res3: Seq[net.ssanj.describe.api.MethodInfo] = List(MethodInfo(method equals), MethodInfo(method toString), MethodInfo(method hashCode), MethodInfo(method canEqual), MethodInfo(method productIterator), MethodInfo(method productElement), MethodInfo(method productArity), MethodInfo(method productPrefix), MethodInfo(method copy$default$1), MethodInfo(method copy), MethodInfo(method failed), MethodInfo(method recover), MethodInfo(method filter), MethodInfo(method map), MethodInfo(method foreach), MethodInfo(method flatten), MethodInfo(method flatMap), MethodInfo(method get), MethodInfo(method recoverWith), MethodInfo(method isSuccess), MethodInfo(method isFailure), MethodInfo(constructor Success), MethodInfo(value value), MethodInfo(method $init$), MethodInfo(method transform), MethodInfo(m...
```

To format the result into a readable output use one of the __d__ methods:

```
res3.d1
```

which yields:

```
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

The ```d1``` method is just the first default formatting which sorts the results into separate numbered lines. There is also a ```d2``` that sorts the results into lines without numbering. The general rule is that if you want to use describe as an API, you can directly manipulate api.MethodInfo. Alternatively if you just want display some results to STDOUT, use the __d__ methods or write your own formatting. More on that later.

If you did have an instance of a type you could achieve the same result with:

```
scala> val s = scala.util.Try(1)
s: scala.util.Try[Int] = Success(1)
methods(s).d1
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
declaredOn[scala.util.Success[_]].d1
```

which yields:

```
 1. [constructor] def Success(value: T): scala.util.Success[T]
 2. def canEqual(x$1: Any): Boolean
 3. def copy[T](value: T): scala.util.Success[T]
 4. def copy$default$1[T]: T @scala.annotation.unchecked.uncheckedVariance
 5. def equals(x$1: Any): Boolean
 6. def failed: scala.util.Try[Throwable]
 7. def filter(p: T => Boolean): scala.util.Try[T]
 8. def flatMap[U](f: T => scala.util.Try[U]): scala.util.Try[U]
 9. def flatten[U](ev: <:<[T,scala.util.Try[U]]): scala.util.Try[U]
10. def foreach[U](f: T => U): Unit
11. def get: T
12. def hashCode: Int
13. def isFailure: Boolean
14. def isSuccess: Boolean
15. def map[U](f: T => U): scala.util.Try[U]
16. def productArity: Int
17. def productElement(x$1: Int): Any
18. def productIterator: Iterator[Any]
19. def productPrefix: String
20. def recover[U >: T](rescueException: PartialFunction[Throwable,U]): scala.util.Try[U]
21. def recoverWith[U >: T](f: PartialFunction[Throwable,scala.util.Try[U]]): scala.util.Try[U]
22. def toString: String
23. def value: T
```

What if you wanted to see only the implicit methods defined within a type? You could use the __implicits__ method:

```
implicits[scala.Predef.type].d1
```

which yields:

```
 1. implicit def $conforms[A]: <:<[A,A]
 2. implicit def ArrayCharSequence(__arrayOfChars: Array[Char]): ArrayCharSequence
 3. implicit def ArrowAssoc[A](self: A): ArrowAssoc[A]
 4. implicit def Boolean2boolean(x: Boolean): Boolean
 5. implicit def Byte2byte(x: Byte): Byte
 6. implicit def Character2char(x: Character): Char
 7. implicit def Double2double(x: Double): Double
 8. implicit def Ensuring[A](self: A): Ensuring[A]
 9. implicit def Float2float(x: Float): Float
10. implicit def Integer2int(x: Integer): Int
11. implicit def Long2long(x: Long): Long
12. implicit def RichException(self: Throwable): RichException
13. implicit def SeqCharSequence(__sequenceOfChars: IndexedSeq[Char]): SeqCharSequence
14. implicit def Short2short(x: Short): Short
15. implicit def StringCanBuildFrom: scala.collection.generic.CanBuildFrom[String,Char,String]
16. implicit def StringFormat[A](self: A): StringFormat[A]
17. implicit def any2stringadd[A](self: A): any2stringadd[A]
18. implicit def augmentString(x: String): scala.collection.immutable.StringOps
19. implicit def boolean2Boolean(x: Boolean): Boolean
20. implicit def booleanArrayOps(xs: Array[Boolean]): scala.collection.mutable.ArrayOps[Boolean]
21. implicit def booleanWrapper(x: Boolean): scala.runtime.RichBoolean
22. implicit def byte2Byte(x: Byte): Byte
23. implicit def byteArrayOps(xs: Array[Byte]): scala.collection.mutable.ArrayOps[Byte]
24. implicit def byteWrapper(x: Byte): scala.runtime.RichByte
25. implicit def char2Character(x: Char): Character
26. implicit def charArrayOps(xs: Array[Char]): scala.collection.mutable.ArrayOps[Char]
27. implicit def charWrapper(c: Char): scala.runtime.RichChar
28. implicit def double2Double(x: Double): Double
29. implicit def doubleArrayOps(xs: Array[Double]): scala.collection.mutable.ArrayOps[Double]
30. implicit def doubleWrapper(x: Double): scala.runtime.RichDouble
31. implicit def fallbackStringCanBuildFrom[T]: scala.collection.generic.CanBuildFrom[String,T,scala.collection.immutable.IndexedSeq[T]]
32. implicit def float2Float(x: Float): Float
33. implicit def floatArrayOps(xs: Array[Float]): scala.collection.mutable.ArrayOps[Float]
34. implicit def floatWrapper(x: Float): scala.runtime.RichFloat
35. implicit def genericArrayOps[T](xs: Array[T]): scala.collection.mutable.ArrayOps[T]
36. implicit def genericWrapArray[T](xs: Array[T]): scala.collection.mutable.WrappedArray[T]
37. implicit def int2Integer(x: Int): Integer
38. implicit def intArrayOps(xs: Array[Int]): scala.collection.mutable.ArrayOps[Int]
39. implicit def intWrapper(x: Int): scala.runtime.RichInt
40. implicit def long2Long(x: Long): Long
41. implicit def longArrayOps(xs: Array[Long]): scala.collection.mutable.ArrayOps[Long]
42. implicit def longWrapper(x: Long): scala.runtime.RichLong
43. implicit def refArrayOps[T <: AnyRef](xs: Array[T]): scala.collection.mutable.ArrayOps[T]
44. implicit def short2Short(x: Short): Short
45. implicit def shortArrayOps(xs: Array[Short]): scala.collection.mutable.ArrayOps[Short]
46. implicit def shortWrapper(x: Short): scala.runtime.RichShort
47. implicit def tuple2ToZippedOps[T1, T2](x: (T1, T2)): runtime.Tuple2Zipped.Ops[T1,T2]
48. implicit def tuple3ToZippedOps[T1, T2, T3](x: (T1, T2, T3)): runtime.Tuple3Zipped.Ops[T1,T2,T3]
49. implicit def unaugmentString(x: scala.collection.immutable.StringOps): String
50. implicit def unitArrayOps(xs: Array[Unit]): scala.collection.mutable.ArrayOps[Unit]
51. implicit def unwrapString(ws: scala.collection.immutable.WrappedString): String
52. implicit def wrapBooleanArray(xs: Array[Boolean]): scala.collection.mutable.WrappedArray[Boolean]
53. implicit def wrapByteArray(xs: Array[Byte]): scala.collection.mutable.WrappedArray[Byte]
54. implicit def wrapCharArray(xs: Array[Char]): scala.collection.mutable.WrappedArray[Char]
55. implicit def wrapDoubleArray(xs: Array[Double]): scala.collection.mutable.WrappedArray[Double]
56. implicit def wrapFloatArray(xs: Array[Float]): scala.collection.mutable.WrappedArray[Float]
57. implicit def wrapIntArray(xs: Array[Int]): scala.collection.mutable.WrappedArray[Int]
58. implicit def wrapLongArray(xs: Array[Long]): scala.collection.mutable.WrappedArray[Long]
59. implicit def wrapRefArray[T <: AnyRef](xs: Array[T]): scala.collection.mutable.WrappedArray[T]
60. implicit def wrapShortArray(xs: Array[Short]): scala.collection.mutable.WrappedArray[Short]
61. implicit def wrapString(s: String): scala.collection.immutable.WrappedString
62. implicit def wrapUnitArray(xs: Array[Unit]): scala.collection.mutable.WrappedArray[Unit]
```

_We use Predef.type here to specify the class for the Predef object (ModuleClass)._

Sometimes you want not only the methods defined on a type but also methods made accessibly through implicit conversions on the types companion object. If we use __methods__ on Option:

```
methods[scala.Option[_]].d1
```

we get:

```
 1. def !=(x$1: Any): Boolean
 2. def ##: Int
 3. def $asInstanceOf[T0]: T0
 4. def $init$: Unit
 5. def $isInstanceOf[T0]: Boolean
 6. def ==(x$1: Any): Boolean
 7. [constructor] def Option: Option[A]
 8. def asInstanceOf[T0]: T0
 9. def canEqual(that: Any): Boolean
10. def clone: java.lang.Object
11. def collect[B](pf: PartialFunction[A,B]): Option[B]
12. def contains[A1 >: A](elem: A1): Boolean
13. def eq(x$1: AnyRef): Boolean
14. def equals(x$1: Any): Boolean
15. def exists(p: A => Boolean): Boolean
16. def filter(p: A => Boolean): Option[A]
17. def filterNot(p: A => Boolean): Option[A]
18. def finalize: Unit
19. def flatMap[B](f: A => Option[B]): Option[B]
20. def flatten[B](ev: <:<[A,Option[B]]): Option[B]
21. def fold[B](ifEmpty: => B)(f: A => B): B
22. def forall(p: A => Boolean): Boolean
23. def foreach[U](f: A => U): Unit
24. def get: A
25. def getClass: java.lang.Class[_]
26. def getOrElse[B >: A](default: => B): B
27. def hashCode: Int
28. def isDefined: Boolean
29. def isEmpty: Boolean
30. def isInstanceOf[T0]: Boolean
31. def iterator: Iterator[A]
32. def map[B](f: A => B): Option[B]
33. def ne(x$1: AnyRef): Boolean
34. def nonEmpty: Boolean
35. def notify: Unit
36. def notifyAll: Unit
37. def orElse[B >: A](alternative: => Option[B]): Option[B]
38. def orNull[A1 >: A](ev: <:<[Null,A1]): A1
39. def productArity: Int
40. def productElement(n: Int): Any
41. def productIterator: Iterator[Any]
42. def productPrefix: String
43. def synchronized[T0](x$1: T0): T0
44. def toLeft[X](right: => X): Product with Serializable with scala.util.Either[A,X]
45. def toList: List[A]
46. def toRight[X](left: => X): Product with Serializable with scala.util.Either[X,A]
47. def toString: java.lang.String
48. def wait: Unit
49. def wait(x$1: Long): Unit
50. def wait(x$1: Long, x$2: Int): Unit
51. def withFilter(p: A => Boolean): Option.this.WithFilter
```

Notice that we don't get any of the fold methods returned. That is because the fold methods are available through an implicit conversion on the Option companion object which converts Option[A] to an Iterable[A]. To also include those methods use __method_+__:

```
methods_+[scala.Option[_]].d1
```

which yields:

```
 1. def !=(x$1: Any): Boolean
 2. def !=(x$1: Any): Boolean
 3. def ##: Int
 4. def ##: Int
 5. def $asInstanceOf[T0]: T0
 6. def $asInstanceOf[T0]: T0
 7. def $init$: Unit
 8. def $init$: Unit
 9. def $isInstanceOf[T0]: Boolean
10. def $isInstanceOf[T0]: Boolean
11. def ++[B >: A, That](that: scala.collection.GenTraversableOnce[B])(bf: scala.collection.generic.CanBuildFrom[Repr,B,That]): That
12. def ++:[B >: A, That](that: Traversable[B])(bf: scala.collection.generic.CanBuildFrom[Repr,B,That]): That
13. def ++:[B >: A, That](that: scala.collection.TraversableOnce[B])(bf: scala.collection.generic.CanBuildFrom[Repr,B,That]): That
14. def /:[B](z: B)(op: (B, A) => B): B
15. def :\[B](z: B)(op: (A, B) => B): B
16. def ==(x$1: Any): Boolean
17. def ==(x$1: Any): Boolean
18. [constructor] def Option: Option[A]
19. def addString(b: StringBuilder): StringBuilder
20. def addString(b: StringBuilder, sep: String): StringBuilder
21. def addString(b: StringBuilder, start: String, sep: String, end: String): StringBuilder
22. def aggregate[B](z: => B)(seqop: (B, A) => B, combop: (B, B) => B): B
23. def asInstanceOf[T0]: T0
24. def asInstanceOf[T0]: T0
25. def canEqual(that: Any): Boolean
26. def canEqual(that: Any): Boolean
27. def clone: java.lang.Object
28. def clone: java.lang.Object
29. def collect[B](pf: PartialFunction[A,B]): Option[B]
30. def collect[B, That](pf: PartialFunction[A,B])(bf: scala.collection.generic.CanBuildFrom[Repr,B,That]): That
31. def collectFirst[B](pf: PartialFunction[A,B]): Option[B]
32. def companion: scala.collection.generic.GenericCompanion[Iterable]
33. def contains[A1 >: A](elem: A1): Boolean
34. def copyToArray[B >: A](xs: Array[B], start: Int, len: Int): Unit
35. def copyToArray[B >: A](xs: Array[B]): Unit
36. def copyToArray[B >: A](xs: Array[B], start: Int): Unit
37. def copyToBuffer[B >: A](dest: scala.collection.mutable.Buffer[B]): Unit
38. def count(p: A => Boolean): Int
39. def drop(n: Int): Repr
40. def dropRight(n: Int): Repr
41. def dropWhile(p: A => Boolean): Repr
42. def eq(x$1: AnyRef): Boolean
43. def eq(x$1: AnyRef): Boolean
44. def equals(x$1: Any): Boolean
45. def equals(x$1: Any): Boolean
46. def exists(p: A => Boolean): Boolean
47. def exists(p: A => Boolean): Boolean
48. def filter(p: A => Boolean): Option[A]
49. def filter(p: A => Boolean): Repr
50. def filterNot(p: A => Boolean): Option[A]
51. def filterNot(p: A => Boolean): Repr
52. def finalize: Unit
53. def finalize: Unit
54. def find(p: A => Boolean): Option[A]
55. def flatMap[B](f: A => Option[B]): Option[B]
56. def flatMap[B, That](f: A => scala.collection.GenTraversableOnce[B])(bf: scala.collection.generic.CanBuildFrom[Repr,B,That]): That
57. def flatten[B](ev: <:<[A,Option[B]]): Option[B]
58. def flatten[B](asTraversable: A => scala.collection.GenTraversableOnce[B]): CC[B]
59. def fold[B](ifEmpty: => B)(f: A => B): B
60. def fold[A1 >: A](z: A1)(op: (A1, A1) => A1): A1
61. def foldLeft[B](z: B)(op: (B, A) => B): B
62. def foldRight[B](z: B)(op: (A, B) => B): B
63. def forall(p: A => Boolean): Boolean
64. def forall(p: A => Boolean): Boolean
65. def foreach[U](f: A => U): Unit
66. def foreach[U](f: A => U): Unit
67. def genericBuilder[B]: scala.collection.mutable.Builder[B,CC[B]]
68. def get: A
69. def getClass: java.lang.Class[_]
70. def getClass: java.lang.Class[_]
71. def getOrElse[B >: A](default: => B): B
72. def groupBy[K](f: A => K): scala.collection.immutable.Map[K,Repr]
73. def grouped(size: Int): Iterator[Repr]
74. def hasDefiniteSize: Boolean
75. def hashCode: Int
76. def hashCode: Int
77. def head: A
78. def headOption: Option[A]
79. def init: Repr
80. def inits: Iterator[Repr]
81. def isDefined: Boolean
82. def isEmpty: Boolean
83. def isEmpty: Boolean
84. def isInstanceOf[T0]: Boolean
85. def isInstanceOf[T0]: Boolean
86. def isTraversableAgain: Boolean
87. def iterator: Iterator[A]
88. def iterator: Iterator[A]
89. def last: A
90. def lastOption: Option[A]
91. def map[B](f: A => B): Option[B]
92. def map[B, That](f: A => B)(bf: scala.collection.generic.CanBuildFrom[Repr,B,That]): That
93. def max[B >: A](cmp: Ordering[B]): A
94. def maxBy[B](f: A => B)(cmp: Ordering[B]): A
95. def min[B >: A](cmp: Ordering[B]): A
96. def minBy[B](f: A => B)(cmp: Ordering[B]): A
97. def mkString: String
98. def mkString(sep: String): String
99. def mkString(start: String, sep: String, end: String): String
100. def ne(x$1: AnyRef): Boolean
101. def ne(x$1: AnyRef): Boolean
102. def newBuilder: scala.collection.mutable.Builder[A,CC[A]]
103. def nonEmpty: Boolean
104. def nonEmpty: Boolean
105. def notify: Unit
106. def notify: Unit
107. def notifyAll: Unit
108. def notifyAll: Unit
109. def orElse[B >: A](alternative: => Option[B]): Option[B]
110. def orNull[A1 >: A](ev: <:<[Null,A1]): A1
111. def par: ParRepr
112. def parCombiner: scala.collection.parallel.Combiner[A,scala.collection.parallel.ParIterable[A]]
113. def partition(p: A => Boolean): (Repr, Repr)
114. def product[B >: A](num: Numeric[B]): B
115. def productArity: Int
116. def productElement(n: Int): Any
117. def productIterator: Iterator[Any]
118. def productPrefix: String
119. def reduce[A1 >: A](op: (A1, A1) => A1): A1
120. def reduceLeft[B >: A](op: (B, A) => B): B
121. def reduceLeftOption[B >: A](op: (B, A) => B): Option[B]
122. def reduceOption[A1 >: A](op: (A1, A1) => A1): Option[A1]
123. def reduceRight[B >: A](op: (A, B) => B): B
124. def reduceRightOption[B >: A](op: (A, B) => B): Option[B]
125. def repr: Repr
126. def reversed: List[A]
127. def sameElements[B >: A](that: scala.collection.GenIterable[B]): Boolean
128. def scan[B >: A, That](z: B)(op: (B, B) => B)(cbf: scala.collection.generic.CanBuildFrom[Repr,B,That]): That
129. def scanLeft[B, That](z: B)(op: (B, A) => B)(bf: scala.collection.generic.CanBuildFrom[Repr,B,That]): That
130. def scanRight[B, That](z: B)(op: (A, B) => B)(bf: scala.collection.generic.CanBuildFrom[Repr,B,That]): That
131. def seq: Iterable[A]
132. def size: Int
133. def slice(from: Int, until: Int): Repr
134. def sliceWithKnownBound(from: Int, until: Int): Repr
135. def sliceWithKnownDelta(from: Int, until: Int, delta: Int): Repr
136. def sliding(size: Int, step: Int): Iterator[Repr]
137. def sliding(size: Int): Iterator[Repr]
138. def span(p: A => Boolean): (Repr, Repr)
139. def splitAt(n: Int): (Repr, Repr)
140. def stringPrefix: String
141. def sum[B >: A](num: Numeric[B]): B
142. def synchronized[T0](x$1: T0): T0
143. def synchronized[T0](x$1: T0): T0
144. def tail: Repr
145. def tails: Iterator[Repr]
146. def take(n: Int): Repr
147. def takeRight(n: Int): Repr
148. def takeWhile(p: A => Boolean): Repr
149. def thisCollection: Iterable[A]
150. def to[Col](cbf: scala.collection.generic.CanBuildFrom[Nothing,A,Col[A @scala.annotation.unchecked.uncheckedVariance]]): Col[A @scala.annotation.unchecked.uncheckedVariance]
151. def toArray[B >: A](evidence$1: scala.reflect.ClassTag[B]): Array[B]
152. def toBuffer[B >: A]: scala.collection.mutable.Buffer[B]
153. def toCollection(repr: Repr): Iterable[A]
154. def toIndexedSeq: scala.collection.immutable.IndexedSeq[A]
155. def toIterable: Iterable[A]
156. def toIterator: Iterator[A]
157. def toLeft[X](right: => X): Product with Serializable with scala.util.Either[A,X]
158. def toList: List[A]
159. def toList: List[A]
160. def toMap[T, U](ev: <:<[A,(T, U)]): scala.collection.immutable.Map[T,U]
161. def toRight[X](left: => X): Product with Serializable with scala.util.Either[X,A]
162. def toSeq: Seq[A]
163. def toSet[B >: A]: scala.collection.immutable.Set[B]
164. def toStream: scala.collection.immutable.Stream[A]
165. def toString: java.lang.String
166. def toString: String
167. def toTraversable: Traversable[A]
168. def toVector: Vector[A]
169. def transpose[B](asTraversable: A => scala.collection.GenTraversableOnce[B]): CC[CC[B] @scala.annotation.unchecked.uncheckedVariance]
170. def unzip[A1, A2](asPair: A => (A1, A2)): (CC[A1], CC[A2])
171. def unzip3[A1, A2, A3](asTriple: A => (A1, A2, A3)): (CC[A1], CC[A2], CC[A3])
172. def view(from: Int, until: Int): scala.collection.IterableView[A,Repr]
173. def view: scala.collection.IterableView[A,Repr]
174. def wait: Unit
175. def wait(x$1: Long): Unit
176. def wait(x$1: Long, x$2: Int): Unit
177. def wait: Unit
178. def wait(x$1: Long): Unit
179. def wait(x$1: Long, x$2: Int): Unit
180. def withFilter(p: A => Boolean): Option.this.WithFilter
181. def withFilter(p: A => Boolean): scala.collection.generic.FilterMonadic[A,Repr]
182. def zip[A1 >: A, B, That](that: scala.collection.GenIterable[B])(bf: scala.collection.generic.CanBuildFrom[Repr,(A1, B),That]): That
```

To list the constructors on an object use __constructors__:

```
constructors[scala.concurrent.duration.Duration.type].d1
```

which yields:

```
1. [constructor] def apply(s: String): scala.concurrent.duration.Duration
2. [constructor] def apply(length: Long, unit: String): scala.concurrent.duration.FiniteDuration
3. [constructor] def apply(length: Long, unit: scala.concurrent.duration.TimeUnit): scala.concurrent.duration.FiniteDuration
4. [constructor] def apply(length: Double, unit: scala.concurrent.duration.TimeUnit): scala.concurrent.duration.Duration
 ```

To list the vals defined in any type use __vals__:

```
vals[scala.Predef.type].d1
```

which yields:

```
 1. val ClassManifest: scala.reflect.ClassManifestFactory.type
 2. val Manifest: scala.reflect.ManifestFactory.type
 3. val Map: scala.collection.immutable.Map.type
 4. val NoManifest: reflect.NoManifest.type
 5. val Set: scala.collection.immutable.Set.type
 6. val StringCanBuildFrom: scala.collection.generic.CanBuildFrom[String,Char,String]
 7. val singleton_<:<: <:<[Any,Any]
 8. val singleton_=:=: =:=[Any,Any]
```

Similarly to list vars in any type use __vars__:

```
vars[scala.concurrent.Channel[_]].d1
```

which yields:

```
1. var lastWritten: Channel.this.LinkedList[A]
2. var nreaders: Int
3. var written: Channel.this.LinkedList[A]
```

To list extractors defined within a type use __extractors__:

```
extractors[scala.concurrent.duration.Duration.type].d1
```

which yields:

```
1. [extractor] def unapply(d: scala.concurrent.duration.Duration): Option[(Long, scala.concurrent.duration.TimeUnit)]
2. [extractor] def unapply(s: String): Option[(Long, scala.concurrent.duration.TimeUnit)]
```

To list classes defined within a type use __classes__:

```
classes[scala.Option[_]].d1
```

which yields:

```
 1. class Option.this.WithFilter
```

To list objects (modules) within a type use __modules__:

```
modules[scala.concurrent.duration.DurationConversions.type]
```

which yields:

```
1. implicit scala.concurrent.duration.DurationConversions.fromNowConvert.type
2. implicit scala.concurrent.duration.DurationConversions.spanConvert.type
```

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