package fr.phpierre.axelordevtools.references.xml

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import fr.phpierre.axelordevtools.util.XmlUtil
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable


class XmlAttributeNameReference(@NotNull element: PsiElement) : PsiReferenceBase<PsiElement?>(element), PsiPolyVariantReference {

    @NotNull
    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project: Project = myElement!!.project
        val modelName: String = XmlUtil.xmlViewRootModelName(element) ?: return arrayOf()
        val range = TextRange(1, element.text.length - 1)
        val key = element.text.substring(range.startOffset, range.endOffset)

        val properties: Set<PsiElement> = XmlUtil.findFieldFromModelName(project, key, modelName)
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

    /*@NotNull
    override fun getVariants(): Array<Any> {
        val project: Project = myElement!!.project
        val properties: List<PsiElement> = SimpleUtil.findProperties(project)
        val variants: MutableList<LookupElement> = ArrayList()
        for (property in properties) {
            if (property.getKey() != null && property.getKey().length() > 0) {
                variants.add(LookupElementBuilder
                        .create(property).withIcon(SimpleIcons.FILE)
                        .withTypeText(property.getContainingFile().getName())
                )
            }
        }
        return variants.toTypedArray()
    }*/

}
