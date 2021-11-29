package fr.phpierre.axelordevtools.references.xml

import com.intellij.patterns.PatternCondition
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext
import fr.phpierre.axelordevtools.contributor.xml.FieldCompletionContributor
import org.jetbrains.annotations.NotNull

class DefaultFieldCondition(@NotNull vararg keys: String) :

    PatternCondition<PsiElement>(keys[0]) {
        override fun accepts(
            @NotNull element: PsiElement,
            context: ProcessingContext
        ): Boolean {
            if(element is XmlAttributeValue) {
                // Dummy fields can't target a field in a domain.
                if(FieldCompletionContributor.defaultColumns.contains(element.value))
                    return false
            }
            return true
        }
}