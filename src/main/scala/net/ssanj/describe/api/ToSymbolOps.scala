package net.ssanj.describe.api

import scala.reflect.runtime.universe.Symbol

trait ToSymbolOps {

  private[api] def toSymbolOps(symbol: Symbol) = new SymbolOps {

    val name: String                      = getName(symbol)

    val fullName: String                  = symbol.fullName

    val typeSignature: MemberInfo         = MemberInfo(symbol.typeSignature)

    val isModule: Boolean                 = symbol.isModule

    val isType: Boolean                   = symbol.isType

    val isAbstract: Boolean               = symbol.isAbstract

    val isAbstractOverride: Boolean       = symbol.isAbstractOverride

    val isClass: Boolean                  = symbol.isClass

    val isConstructor: Boolean            = symbol.isConstructor

    val isFinal: Boolean                  = symbol.isFinal

    val isImplementationArtifact: Boolean = symbol.isImplementationArtifact

    val isImplicit: Boolean               = symbol.isImplicit

    val isJava: Boolean                   = symbol.isJava

    val isMacro: Boolean                  = symbol.isMacro

    val isMethod: Boolean                 = symbol.isMethod

    val isModuleClass: Boolean            = symbol.isModuleClass

    val isPackage: Boolean                = symbol.isPackage

    val isPackageClass: Boolean           = symbol.isPackageClass

    val isParameter: Boolean              = symbol.isParameter

    val isPrivate: Boolean                = symbol.isPrivate

    val isPrivateThis: Boolean            = symbol.isPrivateThis

    val isProtected: Boolean              = symbol.isProtected

    val isProtectedThis: Boolean          = symbol.isProtectedThis

    val isPublic: Boolean                 = symbol.isPublic

    val isSpecialized: Boolean            = symbol.isSpecialized

    val isStatic: Boolean                 = symbol.isStatic

    val isSynthetic: Boolean              = symbol.isSynthetic

    val isTerm: Boolean                   = symbol.isTerm
  }
}