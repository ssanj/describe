How do we represent a symbol?
 One of four types?
 - ValInfo
 - VarInfo
 - MethodInfo
 - ClassInfo
 - TypeInfo (subsumed by ClassInfo?)
 - ParamInfo (??)

How do we represent a Type?
- MemberInfo. Possibly rename to TypeInfo

What's the difference between a ClassSymbol and a Type?
 One's a symbol and the other a type. A ClassSymbol <: TypeSymbol.

How do we convert from a symbol to a Type?
symbol.asType.toType
symbol.typeSignature (for parameters)

What is a safe way to convert from Type to a TypeSymbol and TermSymbol.
Option[TypeInfo] (ClassInfo, Module Info), Option[TermInfo] (ValInfo, VarInfo, MethodInfo)


