package fr.phpierre.axelordevtools

import com.intellij.ide.highlighter.XmlFileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.ProjectScope
import com.intellij.psi.tree.xml.IXmlElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlFile
import fr.phpierre.axelordevtools.references.xml.XmlNameReferenceContributor.Companion.MODULE_PACKAGE


class XmlUtil {
    companion object {
        fun findProperties(project: Project, key: String, modelName: String): List<PsiElement> {
            val result: MutableList<PsiElement> = ArrayList<PsiElement>()

            val virtualFiles = FileTypeIndex.getFiles(XmlFileType.INSTANCE, ProjectScope.getProjectScope(project))

            for (virtualFile in virtualFiles) {
                if(virtualFile.parent.name == "domains") {
                    val file: XmlFile = PsiManager.getInstance(project).findFile(virtualFile) as XmlFile
                    //PsiTreeUtil.getChildOfType(file, MODULE_PACKAGE);\
                    // https://github.com/JetBrains/intellij-sdk-code-samples/blob/516ca8f264d3fc3fbb83cc596b81d1ea0dfa9b90/simple_language_plugin/src/main/java/org/intellij/sdk/language/psi/SimpleNamedElement.java#L7
                    // https://github.com/JetBrains/intellij-sdk-code-samples/blob/516ca8f264d3fc3fbb83cc596b81d1ea0dfa9b90/simple_language_plugin/src/main/gen/org/intellij/sdk/language/psi/SimpleProperty.java#L9
                    // https://github.com/JetBrains/intellij-sdk-code-samples/blob/516ca8f264d3fc3fbb83cc596b81d1ea0dfa9b90/simple_language_plugin/src/main/java/org/intellij/sdk/language/SimpleUtil.java
                    // https://plugins.jetbrains.com/docs/intellij/reference-contributor.html#define-a-named-element-class

                    val attrs = PsiTreeUtil.findChildrenOfType(file, XmlAttributeValue::class.java)

                    for (attr in attrs) {
                        if(attr.value == key) {
                            println(attr) //PsiTreeUtil.findChildOfType()
                        }
                    }
                }
            }
            println(result.size)
            return result
        }
    }

}