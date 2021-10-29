package fr.phpierre.axelordevtools.util

import com.intellij.ide.highlighter.XmlFileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.ProjectScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.structuralsearch.impl.matcher.GlobalMatchingVisitor
import com.intellij.util.indexing.FileBasedIndex
import fr.phpierre.axelordevtools.indexes.DomainPackageIndex
import fr.phpierre.axelordevtools.indexes.ViewNameIndex


class XmlUtil(matchingVisitor: GlobalMatchingVisitor) {

    companion object {
        fun findFieldsFromModelName(
            project: Project,
            key: String,
            modelName: String
        ): List<PsiElement> {
            val result: MutableList<PsiElement> = ArrayList()

            val files = FileBasedIndex.getInstance().getContainingFiles(DomainPackageIndex.KEY, modelName, ProjectScope.getProjectScope(project))

            for (virtualFile in files) {
                val file: PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)

                result.addAll(getFieldsFromFile(file, key))
            }
            return result
        }

        private fun getFieldsFromFile(
            file: PsiFile?,
            key: String
        ): MutableList<PsiElement> {
            val results: MutableList<PsiElement> = ArrayList()

            val rootTag: XmlTag? = (file as XmlFile).rootTag
            val entity: XmlTag = rootTag!!.findFirstSubTag("entity")!!

            entity.subTags.forEach { tag ->
                tag.attributes.forEach { attribute ->
                    if (attribute.value == key) {
                        results.add(attribute)
                    }
                }
            }

            return results
        }

        fun xmlViewRootModelName(element: PsiElement): String? {
            var elementToExplore = PsiTreeUtil.getParentOfType(element, XmlTag::class.java)

            while(elementToExplore != null && elementToExplore.getAttributeValue("model") == null) {
                elementToExplore = PsiTreeUtil.getParentOfType(elementToExplore, XmlTag::class.java)
            }

            return elementToExplore?.getAttributeValue("model")
        }

        fun getPackageNameOfDomain(psiFile: PsiFile): String? {
            if(psiFile !is XmlFile) {
                return null
            }

            val rootTag: XmlTag? = psiFile.rootTag

            val module: XmlTag? = rootTag?.findFirstSubTag("module")
            val aPackage: String? = module?.getAttributeValue("package")

            val entity: XmlTag? = rootTag?.findFirstSubTag("entity")
            val name: String? = entity?.getAttributeValue("name")

            if(name == null || aPackage == null) {
                return null
            }

            return "$aPackage.$name"
        }

        fun indexAxelorViewName(psiFile: PsiFile): MutableSet<String> {
            val results: MutableSet<String> = mutableSetOf()
            if(psiFile !is XmlFile) {
                return results
            }

            val rootTag: XmlTag? = psiFile.rootTag
            rootTag?.subTags?.forEach { tag ->
                val nameAttr = tag.getAttribute("name")
                if(nameAttr != null && nameAttr.value != null) {
                    results.add(nameAttr.value!!)
                }
            }

            return results
        }

        fun getFieldsFromDomain(psiFile: PsiFile): MutableSet<String> {
            val results: MutableSet<String> = mutableSetOf()
            if(psiFile !is XmlFile) {
                return results
            }

            val rootTag: XmlTag? = psiFile.rootTag
            val entity: XmlTag = rootTag!!.findFirstSubTag("entity")!!

            entity.let { entity ->
                entity.subTags.let { tags ->
                    tags.forEach { tag ->
                        val nameAttr = tag.getAttribute("name")
                        nameAttr?.let {
                            it.value?.let { value -> results.add(value)
                            }
                        }
                    }
                }
            }
            return results
        }

        fun findViewFromName(project: Project, viewName: String): List<PsiElement> {
            val result: MutableList<PsiElement> = ArrayList()

            val files = FileBasedIndex.getInstance().getContainingFiles(ViewNameIndex.KEY, viewName, ProjectScope.getProjectScope(project))

            for (virtualFile in files) {
                val file: PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)
                val rootTag: XmlTag? = (file as XmlFile).rootTag

                rootTag?.subTags?.forEach { tag ->
                    val name: XmlAttribute? = tag.getAttribute("name")
                    if(name != null && name.value == viewName) {
                        result.add(name)
                    }
                }
            }
            return result
        }

        fun findDomainFromPackage(project: Project, domainName: String): List<PsiElement> {
            val result: MutableList<PsiElement> = ArrayList()

            val files = FileBasedIndex.getInstance().getContainingFiles(DomainPackageIndex.KEY, domainName, ProjectScope.getProjectScope(project))
            for (virtualFile in files) {
                val psiFile: PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)
                val rootTag: XmlTag? = (psiFile as XmlFile).rootTag

                rootTag?.let {
                    it.findFirstSubTag("entity")?.let { entity ->
                        result.add(entity)
                    }
                }
            }
            return result
        }
    }

}