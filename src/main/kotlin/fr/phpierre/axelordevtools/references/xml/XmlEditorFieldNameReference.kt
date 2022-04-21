package fr.phpierre.axelordevtools.references.xml

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import fr.phpierre.axelordevtools.util.XmlUtil
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class XmlEditorFieldNameReference(@NotNull element: PsiElement) : PsiReferenceBase<PsiElement?>(element),
    PsiPolyVariantReference {

    @NotNull
    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val results: MutableList<ResolveResult> = ArrayList()

        val project: Project = myElement!!.project
        val modelName: String = XmlUtil.xmlViewRootModelName(element) ?: return arrayOf()
        val range = TextRange(1, element.text.length - 1)
        val key = element.text.substring(range.startOffset, range.endOffset)
        val parent = XmlUtil.xmlParentField(element.parent.parent)

        parent?.let {
            val properties: Set<PsiElement> = XmlUtil.findFieldInModelNameAndParents(project, "$parent.$key", modelName, XmlUtil.FieldSearch.MATCH)
            for (property in properties) {
                results.add(PsiElementResolveResult(property))
            }
        }

        return results.toTypedArray()
    }

    @Nullable
    override fun resolve(): PsiElement? {
        val resolveResults: Array<ResolveResult> = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

}
