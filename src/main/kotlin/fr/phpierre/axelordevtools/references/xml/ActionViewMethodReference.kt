package fr.phpierre.axelordevtools.references.xml

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.util.ReferenceSetBase
import com.intellij.psi.xml.XmlAttributeValue
import fr.phpierre.axelordevtools.util.XmlUtil
import org.jetbrains.annotations.NotNull

class ActionViewMethodReference(@NotNull element: PsiElement, @NotNull private val textRange: TextRange) : ReferenceSetBase<PsiReference?>(element),
    PsiPolyVariantReference {
    override fun getRangeInElement(): TextRange {
        return textRange
    }

    override fun resolve(): PsiElement? {
        val resolveResults: Array<ResolveResult> = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun getCanonicalText(): String {
        return element.text
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        TODO("Not yet implemented")
    }

    override fun bindToElement(element: PsiElement): PsiElement {
        TODO("Not yet implemented")
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        return element is XmlAttributeValue
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val results: MutableList<ResolveResult> = ArrayList()

        val project: Project = element.project
        val properties: Set<PsiElement> = XmlUtil.findAction(project, element.text.substring(textRange.startOffset, textRange.endOffset))
        for (property in properties) {
            results.add(PsiElementResolveResult(property))
        }

        return results.toTypedArray()
    }

}