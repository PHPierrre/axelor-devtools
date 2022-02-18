package fr.phpierre.axelordevtools.references.xml

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.util.XmlUtil
import org.jetbrains.annotations.NotNull

class AxelorDomainReference(@NotNull element: PsiElement) : PsiReferenceBase<PsiElement?>(element), PsiPolyVariantReference {

    override fun resolve(): PsiElement? {
        val resolveResults: Array<ResolveResult> = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val results: MutableList<ResolveResult> = ArrayList()

        val project: Project = myElement!!.project
        val range = TextRange(1, element.text.length - 1)
        var domainName = element.text.substring(range.startOffset, range.endOffset)

        if(!XmlUtil.isFullyQualifiedName(domainName)) {
            val rootTag: XmlTag = element.parent.parent.parent.parent as XmlTag
            XmlUtil.resolveFQN(rootTag, domainName)?.let {
                domainName = it
            }
        }

        val properties: List<PsiElement> = XmlUtil.findDomainFromPackage(project, domainName)
        for (property in properties) {
            results.add(PsiElementResolveResult(property))
        }

        return results.toTypedArray()
    }

}
