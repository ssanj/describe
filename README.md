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
methods[Type]
```

For example to list the methods on Option:

```
methods[Option[_]]
```

which yields a Seq of __MethodInfo__ instances:

```
res46: Seq[net.ssanj.describe.api.MethodInfo] = List(MethodInfo(method toLeft), MethodInfo(method toRight), MethodInfo(method toList), MethodInfo(method iterator), MethodInfo(method orElse), MethodInfo(method collect), MethodInfo(method foreach), MethodInfo(method forall), MethodInfo(method exists), MethodInfo(method contains), MethodInfo(method withFilter), MethodInfo(method nonEmpty), MethodInfo(method filterNot), MethodInfo(method filter), MethodInfo(method flatten), MethodInfo(method flatMap), MethodInfo(method fold), MethodInfo(method map), MethodInfo(method orNull), MethodInfo(method getOrElse), MethodInfo(method isDefined), MethodInfo(constructor Option), MethodInfo(method productPrefix), MethodInfo(method productIterator), MethodInfo(method $init$), MethodInfo(method $asInstanceOf), MethodInfo(method $isInstanceOf), MethodInfo(method synchronized), MethodInfo(method ##), MethodInfo(method !=), MethodInfo(method ==), MethodInfo(method ne), MethodInfo(method eq), MethodInfo(method notifyAll), MethodInfo(method notify), MethodInfo(method clone), MethodInfo(method getClass), MethodInfo(method hashCode), MethodInfo(method toString), MethodInfo(method equals), MethodInfo(method wait), MethodInfo(method wait), MethodInfo(method wait), MethodInfo(method finalize), MethodInfo(method asInstanceOf), MethodInfo(method isInstanceOf), MethodInfo(method get), MethodInfo(method isEmpty), MethodInfo(method productArity), MethodInfo(method productElement), MethodInfo(method canEqual))
```

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