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

How do we convert from a Type to a symbol?
type.typeSymbol or
type.termSymbol

What is a safe way to convert from Type to a TypeSymbol and TermSymbol.
Option[TypeInfo] (ClassInfo, Module Info), Option[TermInfo] (ValInfo, VarInfo, MethodInfo)

Should we only list public entities?


