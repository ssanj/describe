package net.ssanj.describe.api

trait SymbolOps {
  val name: String
  val fullName: String
  val typeSignature: MemberInfo
  // val overrides: ???
  // val owner: ???
  // val pos
  val isModule: Boolean
  val isType: Boolean
  val isAbstract: Boolean
  val isAbstractOverride: Boolean
  val isClass: Boolean
  val isConstructor: Boolean
  val isFinal: Boolean
  val isImplementationArtifact: Boolean
  val isImplicit: Boolean
  val isJava: Boolean
  val isMacro: Boolean
  val isMethod: Boolean
  val isModuleClass: Boolean
  val isPackage: Boolean
  val isPackageClass: Boolean
  val isParameter: Boolean
  val isPrivate: Boolean
  val isPrivateThis: Boolean
  val isProtected: Boolean
  val isProtectedThis: Boolean
  val isPublic: Boolean
  val isSpecialized: Boolean
  val isStatic: Boolean
  val isSynthetic: Boolean
  val isTerm: Boolean
}