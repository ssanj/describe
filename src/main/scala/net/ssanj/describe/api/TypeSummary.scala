package net.ssanj.describe.api

class TypeSummary(val mi: MemberInfo) {
   def fullName: String = mi.fullName
   def superclasses: Seq[MemberInfo] = mi.superclasses
   def constructors: Seq[MethodInfo] = mi.constructors
   def extractors: Seq[MethodInfo] = mi.extractors
   def methods: Seq[MethodInfo] = mi.methods
   def vals: Seq[ValInfo] = mi.vals
   def vars: Seq[VarInfo] = mi.vars
   def subclasses: Seq[MemberInfo] = mi.subclasses
}

object TypeSummary {
  implicit def typeSummaryShow(implicit showMemberInfo: Show[MemberInfo],
                                        orderMemberInfo: Ordering[MemberInfo],
                                        showMethodInfo: Show[MethodInfo],
                                        orderMethodInfo: Ordering[MethodInfo],
                                        showValInfo: Show[ValInfo],
                                        orderingValInfo: Ordering[ValInfo],
                                        showVarInfo: Show[VarInfo],
                                        orderingVarInfo: Ordering[VarInfo]): Show[TypeSummary] =
    Show.create { tp =>

      val mi = tp.mi

      def printSuperclasses: String =
        tp.superclasses.map(mi => s"${mi.fullName}").mkString(" -> ")

      def printConstructors: String = tp.constructors.sorted.num

      def fromCompanion[T: Ordering : Show](f: MemberInfo => Seq[T])(implicit seqToFormat: Seq[T] => Format[T]): String = {
        if (!mi.isModuleClass) {
          (mi.companion.map(cmi => f(cmi)).toSeq.flatten).sorted.num
        } else "-"
      }

      def printCompanionConstructors: String = fromCompanion(_.constructors)

      def printExtractors: String = tp.extractors.sorted.num

      def printCompanionExtractors: String = fromCompanion(_.extractors)

      def printMethods: String = tp.methods.sorted.num

      def printCompanionMethods: String = fromCompanion(_.methods)

      def printImplicitMethods: String = mi.implicitMethods.sorted.num

      def printCompanionImplicitMethods: String = fromCompanion(_.implicitMethods)

      def printVals: String = tp.vals.sorted.num

      def printVars: String = tp.vars.sorted.num

      def printSubclasses: String = tp.subclasses.sorted.num

      Seq(
        s"Type: ${tp.fullName}",
        "",
        "Superclasses:",
        s"${printSuperclasses}",
        "",
        "Constructors:",
        s"${printConstructors}",
        "",
        "Constructors (companion):",
        s"${printCompanionConstructors}",
        "",
        "Extractors:",
        s"${printExtractors}",
        "",
        "Extractors (companion):",
        s"${printCompanionExtractors}",
        "",
        "Implicit Methods:",
        s"${printImplicitMethods}",
        "",
        "Implicit Methods (companion):",
        s"${printCompanionImplicitMethods}",
        "",
        "Methods:",
        s"${printMethods}",
        "",
        "Methods (companion):",
        s"${printCompanionMethods}",
        "",
        "Vals:",
        s"${printVals}",
        "",
        "Vars:",
        s"${printVars}",
        "",
        "Subclasses:",
        s"${printSubclasses}"
      ).mkString("\n")
    }

}