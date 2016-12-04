at the Type level:
==================================

=:=            dealias        exists            members             substituteTypes   typeSymbol
asInstanceOf   decl           finalResultType   normalize           takesTypeArgs     weak_<:<
asSeenFrom     declaration    find              orElse              termSymbol        widen
baseClasses    declarations   foreach           paramLists          toString
baseType       decls          isInstanceOf      paramss             typeArgs
companion      erasure        map               resultType          typeConstructor
contains       etaExpand      member            substituteSymbols   typeParams

at the symbol level (Symbol)

NameType               fullName                   isModule          isType
allOverriddenSymbols   info                       isModuleClass     map
alternatives           infoIn                     isPackage         name
annotations            isAbstract                 isPackageClass    orElse
asClass                isAbstractOverride         isParameter       overrides
asInstanceOf           isClass                    isPrivate         owner
asMethod               isConstructor              isPrivateThis     pos
asModule               isFinal                    isProtected       privateWithin
asTerm                 isImplementationArtifact   isProtectedThis   suchThat
asType                 isImplicit                 isPublic          toString
associatedFile         isInstanceOf               isSpecialized     typeSignature
companion              isJava                     isStatic          typeSignatureIn
companionSymbol        isMacro                    isSynthetic
filter                 isMethod                   isTerm

at the term level (TermSymbol)

NameType               infoIn                     isModuleClass        isType
accessed               isAbstract                 isOverloaded         isVal
allOverriddenSymbols   isAbstractOverride         isPackage            isVar
alternatives           isAccessor                 isPackageClass       map
annotations            isByNameParam              isParamAccessor      name
asClass                isCaseAccessor             isParamWithDefault   orElse
asInstanceOf           isClass                    isParameter          overrides
asMethod               isConstructor              isPrivate            owner
asModule               isFinal                    isPrivateThis        pos
asTerm                 isGetter                   isProtected          privateWithin
asType                 isImplementationArtifact   isProtectedThis      setter
associatedFile         isImplicit                 isPublic             suchThat
companion              isInstanceOf               isSetter             toString
companionSymbol        isJava                     isSpecialized        typeSignature
filter                 isLazy                     isStable             typeSignatureIn
fullName               isMacro                    isStatic
getter                 isMethod                   isSynthetic
info                   isModule                   isTerm


at the method level (MethodSymbol)
==================================

NameType               infoIn                     isOverloaded           isVal
accessed               isAbstract                 isPackage              isVar
allOverriddenSymbols   isAbstractOverride         isPackageClass         isVarargs
alternatives           isAccessor                 isParamAccessor        map
annotations            isByNameParam              isParamWithDefault     name
asClass                isCaseAccessor             isParameter            orElse
asInstanceOf           isClass                    isPrimaryConstructor   overrides
asMethod               isConstructor              isPrivate              owner
asModule               isFinal                    isPrivateThis          paramLists
asTerm                 isGetter                   isProtected            paramss
asType                 isImplementationArtifact   isProtectedThis        pos
associatedFile         isImplicit                 isPublic               privateWithin
companion              isInstanceOf               isSetter               returnType
companionSymbol        isJava                     isSpecialized          setter
exceptions             isLazy                     isStable               suchThat
filter                 isMacro                    isStatic               toString
fullName               isMethod                   isSynthetic            typeParams
getter                 isModule                   isTerm                 typeSignature
info                   isModuleClass              isType                 typeSignatureIn


at the type symbol level (TypeSymbol)

NameType               fullName                   isModule          isType
allOverriddenSymbols   info                       isModuleClass     map
alternatives           infoIn                     isPackage         name
annotations            isAbstract                 isPackageClass    orElse
asClass                isAbstractOverride         isParameter       overrides
asInstanceOf           isClass                    isPrivate         owner
asMethod               isConstructor              isPrivateThis     pos
asModule               isFinal                    isProtected       privateWithin
asTerm                 isImplementationArtifact   isProtectedThis   suchThat
asType                 isImplicit                 isPublic          toString
associatedFile         isInstanceOf               isSpecialized     typeSignature
companion              isJava                     isStatic          typeSignatureIn
companionSymbol        isMacro                    isSynthetic
filter                 isMethod                   isTerm


at the class symbol level (ClassSymbol)

NameType               isAbstractOverride         isPackage               orElse
allOverriddenSymbols   isAbstractType             isPackageClass          overrides
alternatives           isAliasType                isParameter             owner
annotations            isCaseClass                isPrimitive             pos
asClass                isClass                    isPrivate               primaryConstructor
asInstanceOf           isConstructor              isPrivateThis           privateWithin
asMethod               isContravariant            isProtected             selfType
asModule               isCovariant                isProtectedThis         suchThat
asTerm                 isDerivedValueClass        isPublic                superPrefix
asType                 isExistential              isSealed                thisPrefix
associatedFile         isFinal                    isSpecialized           toString
baseClasses            isImplementationArtifact   isStatic                toType
companion              isImplicit                 isSynthetic             toTypeConstructor
companionSymbol        isInstanceOf               isTerm                  toTypeIn
filter                 isJava                     isTrait                 typeParams
fullName               isMacro                    isType                  typeSignature
info                   isMethod                   knownDirectSubclasses   typeSignatureIn
infoIn                 isModule                   map
isAbstract             isModuleClass              module
isAbstractClass        isNumeric                  name

at the module symbol level (ModuleSymbol)

NameType               infoIn                     isModuleClass        isType
accessed               isAbstract                 isOverloaded         isVal
allOverriddenSymbols   isAbstractOverride         isPackage            isVar
alternatives           isAccessor                 isPackageClass       map
annotations            isByNameParam              isParamAccessor      moduleClass
asClass                isCaseAccessor             isParamWithDefault   name
asInstanceOf           isClass                    isParameter          orElse
asMethod               isConstructor              isPrivate            overrides
asModule               isFinal                    isPrivateThis        owner
asTerm                 isGetter                   isProtected          pos
asType                 isImplementationArtifact   isProtectedThis      privateWithin
associatedFile         isImplicit                 isPublic             setter
companion              isInstanceOf               isSetter             suchThat
companionSymbol        isJava                     isSpecialized        toString
filter                 isLazy                     isStable             typeSignature
fullName               isMacro                    isStatic             typeSignatureIn
getter                 isMethod                   isSynthetic
info                   isModule                   isTerm