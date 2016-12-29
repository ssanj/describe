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

Show[Context, T]

What is the difference in the Repl between a String and writing to standout?
 Strings seem to be constrained by the lenght of vals.isettings.maxPrintString, while println does not.

How do we incorporate sorting into the query dsl?