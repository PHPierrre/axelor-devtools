package fr.phpierre.axelordevtools.references.xml

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import fr.phpierre.axelordevtools.util.XmlUtil
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class AxelorActionReference(@NotNull element: PsiElement) : PsiReferenceBase<PsiElement?>(element),
    PsiPolyVariantReference {

    @NotNull
    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project: Project = myElement!!.project
        val range = TextRange(1, element.text.length - 1)
        val viewName = element.text.substring(range.startOffset, range.endOffset)

        val properties: List<PsiElement> = XmlUtil.findActionReference(project, viewName)
        val results: MutableList<ResolveResult> = ArrayList()
        for (property in properties) {
            results.add(PsiElementResolveResult(property))
        }

        return results.toTypedArray()
    }

    @Nullable
    override fun resolve(): PsiElement? {
        val resolveResults: Array<ResolveResult> = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

}