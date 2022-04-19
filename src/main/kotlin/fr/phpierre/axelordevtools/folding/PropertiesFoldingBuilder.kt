package fr.phpierre.axelordevtools.folding

import com.intellij.find.findUsages.JavaMethodFindUsagesOptions
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.lang.properties.IProperty
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.impl.compiled.ClsClassImpl
import com.intellij.psi.impl.source.PsiClassImpl
import com.intellij.psi.util.PsiTreeUtil
import fr.phpierre.axelordevtools.util.XmlUtil


class PropertiesFoldingBuilder : FoldingBuilderEx() {

    companion object {
        const val APPLICATION_CONFIG_FULLPATH = "com.axelor.app.AppSettings"
    }
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors: MutableList<FoldingDescriptor> = ArrayList()
        val group = FoldingGroup.newGroup("application-properties")

        val literalExpressions: Collection<PsiLiteralExpression> = PsiTreeUtil.findChildrenOfType(
            root,
            PsiLiteralExpression::class.java
        )


        for (literalExpression in literalExpressions) {
            val method = PsiTreeUtil.getParentOfType(literalExpression, PsiMethodCallExpression::class.java)
            method?.let {
                val qualifiedName = method.resolveMethod()?.containingClass?.qualifiedName
                qualifiedName?.let {
                    if(qualifiedName == APPLICATION_CONFIG_FULLPATH && (literalExpression.value as String).isNotEmpty()) {
                        val foldingDescriptor = FoldingDescriptor(literalExpression.node,
                                TextRange(literalExpression.textRange.startOffset + 1, literalExpression.textRange.endOffset - 1),
                                group)
                        descriptors.add(foldingDescriptor)
                    }
                }
            }
        }


        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String {
        val placeholderText = "..."
        if (node.psi is PsiLiteralExpression) {
            val nodeElement = node.psi as PsiLiteralExpression
            val key: String = nodeElement.value as String
            val properties: List<IProperty> = XmlUtil.findProperties(nodeElement.project, key)

            if(properties.isEmpty())
                return placeholderText

            val place: String? = properties[0].value
            val placeFix = place?.replace("\n", "\\n")?.replace("\"", "\\\\\"")
            return placeFix ?: placeholderText
        }
        return placeholderText
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return true
    }
}