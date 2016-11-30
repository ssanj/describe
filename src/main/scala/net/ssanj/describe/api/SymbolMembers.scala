package net.ssanj.describe.api

trait SymbolAttr {
  val symbol: scala.reflect.runtime.universe.Symbol
  lazy val name: String = getName(symbol)
  lazy val fullName: String = symbol.fullName
}

trait SymbolFlags {
  def isAbstract: Boolean
  def isAbstractOverride: Boolean
  def isClass: Boolean
  def isConstructor: Boolean
  def isFinal: Boolean
  def isImplementationArtifact: Boolean
  def isImplicit: Boolean
  def isJava: Boolean
  def isMacro: Boolean
  def isMethod: Boolean
  def isModule: Boolean
  // def isOverloadedMethod: Boolean //protected access
  def isPackage: Boolean
  def isPackageClass: Boolean
  def isParameter: Boolean
  def isPrivate: Boolean
  def isPrivateThis: Boolean
  def isProtected: Boolean
  def isProtectedThis: Boolean
  def isPublic: Boolean
  def isSpecialized: Boolean
  def isStatic: Boolean
  def isSynthetic: Boolean
  def isTerm: Boolean
  def isType: Boolean
  def isModuleClass: Boolean
}

// ============ Term Symbols ========================

trait TermSymbolFlags extends SymbolFlags {
  def isParamAccessor: Boolean
  def isSetter: Boolean
  def isOverloaded: Boolean
  def isParamWithDefault: Boolean
  def isVar: Boolean
  def isCaseAccessor: Boolean
  def isAccessor: Boolean
  def isGetter: Boolean
  def isStable: Boolean
  def isByNameParam: Boolean
  def isLazy: Boolean
  def isVal: Boolean
}

trait MethodSymbolFlags extends TermSymbolFlags {
  def isVarargs: Boolean
  def isPrimaryConstructor: Boolean
}

trait VariableSymbolFlags extends TermSymbolFlags

trait ModuleSymbolFlags extends TermSymbolFlags

// ============ Type Symbols ========================

trait TypeSymbolFlags extends SymbolFlags {
  def isAliasType: Boolean
  def isCovariant: Boolean
  def isContravariant: Boolean
  def isExistential: Boolean
  def isAbstractType: Boolean
}

trait ClassSymbolFlags extends TypeSymbolFlags {
  def isAbstractClass: Boolean
  def isNumeric: Boolean
  def isPrimitive: Boolean
  def isTrait: Boolean
  def isCaseClass: Boolean
  def isDerivedValueClass: Boolean
  def isSealed: Boolean
}