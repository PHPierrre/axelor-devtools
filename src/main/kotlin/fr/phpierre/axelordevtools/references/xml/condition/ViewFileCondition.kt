package fr.phpierre.axelordevtools.references.xml.condition

import com.intellij.patterns.PatternCondition
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull

class ViewFileCondition(@NotNull vararg keys: String) :

    PatternCondition<PsiElement>(keys[0]) {
    override fun accepts(
        @NotNull element: PsiElement,
        context: ProcessingContext
    ): Boolean {
        val path = element.containingFile?.originalFile?.containingDirectory?.name

        return (path == "views")
    }
}
