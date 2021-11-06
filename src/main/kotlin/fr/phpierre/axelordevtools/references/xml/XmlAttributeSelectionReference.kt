package fr.phpierre.axelordevtools.references.xml

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import fr.phpierre.axelordevtools.util.XmlUtil
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class XmlAttributeSelectionReference(@NotNull element: PsiElement) : PsiReferenceBase<PsiElement?>(element),
    PsiPolyVariantReference {

    @Nullable
    override fun resolve(): PsiElement? {
        val resolveResults: Array<ResolveResult> = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project: Project = myElement!!.project
        val range = TextRange(1, element.text.length - 1)
        val selectionName = element.text.substring(range.startOffset, range.endOffset)

        val properties: Set<PsiElement> = XmlUtil.findSelectionName(project, selectionName)
        val results: MutableList<ResolveResult> = ArrayList()
        for (property in properties) {
            results.add(PsiElementResolveResult(property))
        }
        return results.toTypedArray()
    }
}