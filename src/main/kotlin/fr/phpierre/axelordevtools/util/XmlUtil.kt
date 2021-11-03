package fr.phpierre.axelordevtools.util

import com.intellij.lang.properties.IProperty
import com.intellij.lang.properties.PropertiesFileType
import com.intellij.lang.properties.psi.PropertiesFile
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

        /**
         * Find if a field exist in a domain
         */
        fun findFieldFromModelName(
            project: Project,
            key: String,
            modelName: String
        ): Set<PsiElement> {
            val result: MutableSet<PsiElement> = mutableSetOf()

            val files = FileBasedIndex.getInstance().getContainingFiles(DomainPackageIndex.KEY, modelName, ProjectScope.getProjectScope(project))

            for (virtualFile in files) {
                val file: PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)

                result.addAll(getFieldFromFile(file, key))
            }
            return result
        }

        private fun getFieldFromFile(
            file: PsiFile?,
            key: String
        ): Set<PsiElement> {
            val results: MutableSet<PsiElement> = mutableSetOf()

            val rootTag: XmlTag? = (file as XmlFile).rootTag
            val entity: XmlTag = rootTag!!.findFirstSubTag("entity")!!

            entity.getAttribute("extends")?.let {
                it.value?.let { value ->
                    results.addAll(findFieldFromModelName(file.project, key, value))
                }
            }

            entity.subTags.forEach { tag ->
                tag.attributes.forEach { attribute ->
                    if (attribute.value == key) {
                        results.add(attribute)
                    }
                }
            }

            return results
        }

        /**
         * Return the model value of an element in view (could be a field).
         * @param element A field element
         * @return The model value of an element in an Axelor view
         */
        fun xmlViewRootModelName(element: PsiElement): String? {
            var elementToExplore = PsiTreeUtil.getParentOfType(element, XmlTag::class.java)

            while(elementToExplore != null && elementToExplore.getAttributeValue("model") == null) {
                elementToExplore = PsiTreeUtil.getParentOfType(elementToExplore, XmlTag::class.java)
            }

            return elementToExplore?.getAttributeValue("model")
        }

        /**
         * Explore a psiFile (should be a domain) and compute the package with the entity name.
         * @param psiFile The domain file
         * @return The package concatenated with the entity name
         */
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


        /**
         * Explore a psiFile and find the name of every views (grid, form, tree, etc ...).
         * @param psiFile The view file
         * @return A list of every view's name in the file
         */
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

        /**
         * Get fields of a domain and his parent domains.
         * @param psiFile The domain file
         * @return A list with every field found.
         */
        fun getFieldsFromDomain(psiFile: PsiFile): MutableSet<String> {
            val results: MutableSet<String> = mutableSetOf()
            if(psiFile !is XmlFile) {
                return results
            }

            val rootTag: XmlTag? = psiFile.rootTag
            val entity: XmlTag = rootTag!!.findFirstSubTag("entity")!!

            entity.getAttribute("extends")?.let {
                it.value?.let { value ->
                    val files = FileBasedIndex.getInstance().getContainingFiles(DomainPackageIndex.KEY, value, ProjectScope.getProjectScope(psiFile.project))
                    for (virtualFile in files) {
                        val psiFileParent: PsiFile? = PsiManager.getInstance(psiFile.project).findFile(virtualFile)
                        psiFileParent?.let { file -> results.addAll(getFieldsFromDomain(file)) }
                    }
                }
            }

            entity.let {
                it.subTags.let { tags ->
                    tags.forEach { tag ->
                        val nameAttr = tag.getAttribute("name")
                        nameAttr?.let { name ->
                            name.value?.let { value -> results.add(value)
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

        fun findProperties(project: Project, key: String): List<IProperty> {
            val results: MutableList<IProperty> = ArrayList()

            val virtualFiles = FileTypeIndex.getFiles(PropertiesFileType.INSTANCE, ProjectScope.getProjectScope(project))
            for (virtualFile in  virtualFiles){
                // Select only the main application.properties
                if(virtualFile.parent.path.contains("src/main/resources")) {
                    val file = PsiManager.getInstance(project).findFile(virtualFile) as PropertiesFile
                    file.findPropertyByKey(key)?.let {
                        results.add(it)
                    }
                }
            }
            return results
        }
    }

}