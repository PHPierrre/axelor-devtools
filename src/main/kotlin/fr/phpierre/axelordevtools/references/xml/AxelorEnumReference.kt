package fr.phpierre.axelordevtools.references.xml

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.util.AxelorFile
import fr.phpierre.axelordevtools.util.XmlUtil
import org.jetbrains.annotations.NotNull

class AxelorEnumReference(@NotNull element: PsiElement) : PsiReferenceBase<PsiElement?>(element), PsiPolyVariantReference {
    override fun resolve(): PsiElement? {
        val resolveResults: Array<ResolveResult> = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project: Project = myElement!!.project
        val range = TextRange(1, element.text.length - 1)
        var enumName = element.text.substring(range.startOffset, range.endOffset)

        // We check if domain is not FQN and is in a domain
        if(!XmlUtil.isFullyQualifiedName(enumName) && AxelorFile.isDomain(element)) {
            PsiTreeUtil.getParentOfType(element, XmlFile::class.java)?.rootTag?.let { rootTag: XmlTag ->
                XmlUtil.resolveFQN(rootTag, enumName)?.let {
                    enumName = it
                }
            }
        }

        val properties: Set<PsiElement> = XmlUtil.findEnumName(project, enumName)
        val results: MutableList<ResolveResult> = ArrayList()
        for (property in properties) {
            results.add(PsiElementResolveResult(property))
        }

        return results.toTypedArray()
    }
}