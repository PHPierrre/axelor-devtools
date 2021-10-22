package fr.phpierre.axelordevtools.references.xml

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlTagValue
import fr.phpierre.axelordevtools.XmlUtil
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable


class XmlAttributeNameReference(@NotNull element: PsiElement, textRange: TextRange) : PsiReferenceBase<PsiElement?>(element, textRange), PsiPolyVariantReference {
    private val key: String

    @NotNull
    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project: Project = myElement!!.project
        val modelName: String = xmlViewRootModelName(element)
        val properties: List<PsiElement> = XmlUtil.findProperties(project, key, modelName)
        val results: MutableList<ResolveResult> = ArrayList<ResolveResult>()
        for (property in properties) {
            results.add(PsiElementResolveResult(property))
        }
        return results.toTypedArray()
    }

    private fun xmlViewRootModelName(element: PsiElement): String {
        var elementToExplore = PsiTreeUtil.getParentOfType(element, XmlTag::class.java)

        while(elementToExplore?.getAttributeValue("model") == null) {
            elementToExplore = PsiTreeUtil.getParentOfType(elementToExplore?.parent, XmlTag::class.java)
        }

        return elementToExplore.getAttributeValue("model")!!
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

    init {
        key = element.text.substring(textRange.startOffset, textRange.endOffset)
    }
}
