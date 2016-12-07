package net.ssanj.describe.api

import scala.reflect.runtime.{universe => ru}
import org.scalatest.{Matchers, WordSpecLike}

class SampleA

class SampleB { type BType }

object SampleC

final class ReflectTypeSpec extends Matchers with WordSpecLike {

  "A ReflectType" should {
    "categorise a Class" in {
        val t  = ru.typeOf[SampleA]

        t.termSymbol should be (ru.NoSymbol)
        val typeSymbol = t.typeSymbol
        typeSymbol should not (be (ru.NoSymbol))

        getName(typeSymbol) should be ("SampleA")
        typeSymbol.fullName should be ("net.ssanj.describe.api.SampleA")

        typeSymbol.isType                   should be (true)
        typeSymbol.isClass                  should be (true)
        typeSymbol.isPublic                 should be (true)
        typeSymbol.isStatic                 should be (true)

        typeSymbol.isAbstract               should be (false)
        typeSymbol.isAbstractOverride       should be (false)
        typeSymbol.isConstructor            should be (false)
        typeSymbol.isFinal                  should be (false)
        typeSymbol.isImplementationArtifact should be (false)
        typeSymbol.isImplicit               should be (false)
        typeSymbol.isJava                   should be (false)
        typeSymbol.isMacro                  should be (false)
        typeSymbol.isPrivate                should be (false)
        typeSymbol.isPrivateThis            should be (false)
        typeSymbol.isProtected              should be (false)
        typeSymbol.isProtectedThis          should be (false)
        typeSymbol.isSynthetic              should be (false)
        typeSymbol.isTerm                   should be (false)
        typeSymbol.isModule                 should be (false)
        typeSymbol.isPackage                should be (false)
        typeSymbol.isPackageClass           should be (false)
        typeSymbol.isMethod                 should be (false)
        typeSymbol.isModuleClass            should be (false)
        typeSymbol.isParameter              should be (false)

        val classSymbol = typeSymbol.asClass

        classSymbol.isAliasType             should be (false)
        classSymbol.isCaseClass             should be (false)
        classSymbol.isContravariant         should be (false)
        classSymbol.isCovariant             should be (false)
        classSymbol.isDerivedValueClass     should be (false)
        classSymbol.isExistential           should be (false)
        classSymbol.isNumeric               should be (false)
        classSymbol.isPrimitive             should be (false)
        classSymbol.isSealed                should be (false)
        classSymbol.isTrait                 should be (false)
    }
  }

  it should {
    "categories a path-dependent type" in {
      val decls  = ru.typeOf[SampleB].decls

      val symbol = decls.find(s => s.name.decodedName.toString == "BType").get

      symbol.isType                       should be (true)
      val typeSymbol = symbol.asType

      typeSymbol.isPublic                 should be (true)
      typeSymbol.isAbstract               should be (true)

      typeSymbol.isAbstractOverride       should be (false)
      typeSymbol.isClass                  should be (false)
      typeSymbol.isConstructor            should be (false)
      typeSymbol.isFinal                  should be (false)
      typeSymbol.isImplementationArtifact should be (false)
      typeSymbol.isImplicit               should be (false)
      typeSymbol.isJava                   should be (false)
      typeSymbol.isMacro                  should be (false)
      typeSymbol.isPrivate                should be (false)
      typeSymbol.isPrivateThis            should be (false)
      typeSymbol.isProtected              should be (false)
      typeSymbol.isProtectedThis          should be (false)
      typeSymbol.isStatic                 should be (false)
      typeSymbol.isSynthetic              should be (false)
      typeSymbol.isTerm                   should be (false)
      typeSymbol.isModule                 should be (false)
      typeSymbol.isPackage                should be (false)
      typeSymbol.isPackageClass           should be (false)
      typeSymbol.isMethod                 should be (false)
      typeSymbol.isModuleClass            should be (false)
      typeSymbol.isParameter              should be (false)
    }
  }

  it should {
    "categories an object type" in {
      val t  = ru.typeOf[SampleC.type]

      t.termSymbol should not (be (ru.NoSymbol))
      val termSymbol = t.termSymbol.asTerm

      t.typeSymbol should not (be (ru.NoSymbol))
      val typeSymbol = t.typeSymbol.asType

      //when it's a type
      typeSymbol.isType                   should be (true)
      typeSymbol.isPublic                 should be (true)
      typeSymbol.isClass                  should be (true)
      typeSymbol.isStatic                 should be (true)
      typeSymbol.isModuleClass            should be (true)

      typeSymbol.isAbstract               should be (false)
      typeSymbol.isAbstractOverride       should be (false)
      typeSymbol.isConstructor            should be (false)
      typeSymbol.isFinal                  should be (false)
      typeSymbol.isImplementationArtifact should be (false)
      typeSymbol.isImplicit               should be (false)
      typeSymbol.isJava                   should be (false)
      typeSymbol.isMacro                  should be (false)
      typeSymbol.isPrivate                should be (false)
      typeSymbol.isPrivateThis            should be (false)
      typeSymbol.isProtected              should be (false)
      typeSymbol.isProtectedThis          should be (false)
      typeSymbol.isSynthetic              should be (false)
      typeSymbol.isTerm                   should be (false)
      typeSymbol.isModule                 should be (false)
      typeSymbol.isPackage                should be (false)
      typeSymbol.isPackageClass           should be (false)
      typeSymbol.isMethod                 should be (false)
      typeSymbol.isParameter              should be (false)

      //when it's a term
      termSymbol.isTerm                   should be (true)
      termSymbol.isPublic                 should be (true)
      termSymbol.isModule                 should be (true)
      termSymbol.isStable                 should be (true)
      termSymbol.isStatic                 should be (true)

      termSymbol.isAbstract               should be (false)
      termSymbol.isAbstractOverride       should be (false)
      termSymbol.isAccessor               should be (false)
      termSymbol.isByNameParam            should be (false)
      termSymbol.isCaseAccessor           should be (false)
      termSymbol.isClass                  should be (false)
      termSymbol.isConstructor            should be (false)
      termSymbol.isFinal                  should be (false)
      termSymbol.isGetter                 should be (false)
      termSymbol.isImplementationArtifact should be (false)
      termSymbol.isImplicit               should be (false)
      termSymbol.isInstanceOf             should be (false)
      termSymbol.isJava                   should be (false)
      termSymbol.isLazy                   should be (false)
      termSymbol.isMacro                  should be (false)
      termSymbol.isMethod                 should be (false)
      termSymbol.isModuleClass            should be (false)
      termSymbol.isOverloaded             should be (false)
      termSymbol.isPackage                should be (false)
      termSymbol.isPackageClass           should be (false)
      termSymbol.isParamAccessor          should be (false)
      termSymbol.isParamWithDefault       should be (false)
      termSymbol.isParameter              should be (false)
      termSymbol.isPrivate                should be (false)
      termSymbol.isPrivateThis            should be (false)
      termSymbol.isProtected              should be (false)
      termSymbol.isProtectedThis          should be (false)
      termSymbol.isSetter                 should be (false)
      termSymbol.isSpecialized            should be (false)
      termSymbol.isSynthetic              should be (false)
      termSymbol.isType                   should be (false)
      termSymbol.isVal                    should be (false)
      termSymbol.isVar                    should be (false)

    }
  }
}

