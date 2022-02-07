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
import com.intellij.psi.util.PsiUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.structuralsearch.impl.matcher.GlobalMatchingVisitor
import com.intellij.util.indexing.FileBasedIndex
import fr.phpierre.axelordevtools.indexes.*
import fr.phpierre.axelordevtools.lang.XmlTagLang
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.objects.xml.AbstractAction
import fr.phpierre.axelordevtools.objects.xml.XmlParentActionReference
import fr.phpierre.axelordevtools.objects.xml.XmlParentViewReference


class XmlUtil(matchingVisitor: GlobalMatchingVisitor) {

    enum class FieldSearch{
        ALL, MATCH;
    }

    companion object {

        /**
         * Find the existent field in a domain and his parents.
         * Can be used to verify that the key exist in a domain name.
         * @param project The project
         * @param key The field name
         * @param modelName The domain name (package.class)
         * @param searchParam The search parameter
         * @return A set of fields found
         */
        fun findFieldFromModelName(
            project: Project,
            key: String,
            modelName: String,
            searchParam: FieldSearch
        ): Set<PsiElement> {
            val result: MutableSet<PsiElement> = mutableSetOf()

            val files = FileBasedIndex.getInstance().getContainingFiles(DomainPackageIndex.KEY, modelName, ProjectScope.getProjectScope(project))

            for (virtualFile in files) {
                val file: PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)

                result.addAll(getFieldFromFile(file, key, searchParam))
            }
            return result
        }

        /**
         * Find if a field name exist in a file and his parents
         * Can be used to verify that the key exist in a domain name.
         * @param file The domain file
         * @param key The field name
         * @param searchParam The search parameter
         * @return A set of fields found
         */
        private fun getFieldFromFile(
            file: PsiFile?,
            key: String,
            searchParam: FieldSearch
        ): Set<PsiElement> {
            val results: MutableSet<PsiElement> = mutableSetOf()

            val rootTag: XmlTag? = (file as XmlFile).rootTag
            val entity: XmlTag = rootTag!!.findFirstSubTag("entity")!!

            entity.getAttribute("extends")?.let {
                it.value?.let { value ->
                    results.addAll(findFieldFromModelName(file.project, key, value, searchParam))
                }
            }

            // If the field is a relation
            if(key.contains(".")) {
                val field: String = key.substringBefore(".")
                val tail: String = key.substringAfter(".")
                val relations: Set<PsiElement> = searchFieldInEntity(entity, field)
                for (relation in relations) {
                    val ref = (relation.parent as XmlTag).getAttribute("ref")
                    ref?.value?.let {
                        results.addAll(findFieldFromModelName(file.project,tail, it, searchParam))
                    }
                }
            }
            // Else it's a simple field
            else {
                if(searchParam == FieldSearch.MATCH) {
                    results.addAll(searchFieldInEntity(entity, key))
                } else if(searchParam == FieldSearch.ALL) {
                    results.addAll(searchFieldsInEntity(entity))
                }
            }

            return results
        }

        private fun searchFieldInEntity(
            entity: XmlTag,
            key: String
        ): Set<PsiElement> {
            val results: MutableSet<PsiElement> = mutableSetOf()

            entity.subTags.forEach { tag ->
                tag.attributes.forEach { attribute ->
                    if (attribute.value == key) {
                        results.add(attribute)
                    }
                }
            }

            return results
        }

        private fun searchFieldsInEntity(
                entity: XmlTag
        ): Set<PsiElement> {
            val results: MutableSet<PsiElement> = mutableSetOf()

            entity.subTags.forEach { tag ->
                val nameAttr = tag.getAttribute("name")
                nameAttr?.let { name ->
                    results.add(name)
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
                if (XmlTagLang.viewType.contains(tag.name)) {
                    val nameAttr = tag.getAttribute("name")
                    if (nameAttr != null && nameAttr.value != null) {
                        results.add(nameAttr.value!!)
                    }
                }
            }

            return results
        }

        /**
         * Get fields of a domain and his parent domains.
         * @param project The project.
         * @param modelName The model name.
         * @return A list with every field found.
         */
        fun getFieldsFromDomain(project: Project, modelName: String): MutableSet<PsiElement> {
            val results: MutableSet<PsiElement> = mutableSetOf()
            val files = FileBasedIndex.getInstance().getContainingFiles(DomainPackageIndex.KEY, modelName, ProjectScope.getProjectScope(project))
            for (virtualFile in files) {
                val file: PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)
                file?.let { results.addAll(getFieldsFromDomain(file)) }
            }

            return results
        }

        /**
         * Get fields of a domain and his parent domains.
         * @param psiFile The domain file.
         * @return A list with every field found.
         */
        private fun getFieldsFromDomain(psiFile: PsiFile): MutableSet<PsiElement> {
            val results: MutableSet<PsiElement> = mutableSetOf()
            if(psiFile !is XmlFile) {
                return results
            }

            val rootTag: XmlTag? = psiFile.rootTag
            val entity: XmlTag = rootTag!!.findFirstSubTag("entity")!!

            entity.getAttribute("extends")?.let {
                it.value?.let { value ->
                    results.addAll(getFieldsFromDomain(psiFile.project, value))
                }
            }

            results.addAll(searchFieldsInEntity(entity))

            return results
        }

        /**
         * Find where a view is defined in the project
         * @param project The projet
         * @param viewName The view name
         * @return A list of locations of the view in the project
         */
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

        /**
         * Find where a domain is defined in the project
         * @param project The projet
         * @param domainName The domain name (package.class)
         * @return A list of locations of the domain in the project
         */
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

        /**
         * Search the value of a key in the main application.properties file
         * @param project The projet
         * @param key The property key
         * @return A list of defined key found in the property file
         */
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

        /**
         * Search every selection [XmlTag] in a file
         * @param psiFile The file
         * @return A list of selection found
         */
        fun indexSelectionName(psiFile: PsiFile): Set<String> {
            val results: MutableSet<String> = mutableSetOf()
            if(psiFile !is XmlFile) {
                return results
            }

            val rootTag: XmlTag? = psiFile.rootTag

            val selections = rootTag?.findSubTags("selection")

            selections?.forEach { tag ->
                val attr = tag.getAttribute("name")
                attr?.value?.let {
                    results.add(it)
                }
            }

            return results
        }

        fun findSelectionName(project: Project, selectionName: String): Set<PsiElement> {
            val results: MutableSet<PsiElement> = mutableSetOf()
            val files = FileBasedIndex.getInstance().getContainingFiles(SelectionNameIndex.KEY, selectionName, ProjectScope.getProjectScope(project))

            for (virtualFile in files) {
                val file: PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)
                val rootTag: XmlTag? = (file as XmlFile).rootTag

                val selections = rootTag?.findSubTags("selection")

                selections?.forEach { tag ->
                    val attr = tag.getAttribute("name")
                    attr?.value?.let {
                        if(it == selectionName) {
                            results.add(attr)
                        }
                    }
                }
            }
            return results
        }

        /**
         * Explore a psiFile and find the name of every <action-method>.
         * @param psiFile The view file
         * @return A list of every action method's name in the file
         */
        fun indexAxelorActionMethodName(psiFile: PsiFile): Set<String> {
            val results: MutableSet<String> = mutableSetOf()
            if(psiFile !is XmlFile) {
                return results
            }

            val rootTag: XmlTag? = psiFile.rootTag
            rootTag?.subTags?.forEach { tag ->
                if (tag.name == "action-method" || tag.name == "action-group" || tag.name == "action-view" || tag.name == "action-ws") {
                    val nameAttr = tag.getAttribute("name")
                    if(nameAttr != null && nameAttr.value != null) {
                        results.add(nameAttr.value!!)
                    }
                }
            }

            return results
        }

        fun findAction(project: Project, actionName: String): Set<PsiElement> {
            val results: MutableSet<PsiElement> = mutableSetOf()
            val files = FileBasedIndex.getInstance().getContainingFiles(ActionNameIndex.KEY, actionName, ProjectScope.getProjectScope(project))

            for (virtualFile in files) {
                val file: PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)
                val rootTag: XmlTag? = (file as XmlFile).rootTag

                val selections = rootTag?.subTags

                selections?.forEach { tag ->
                    val attr = tag.getAttribute("name")
                    attr?.value?.let {
                        if(it == actionName) {
                            results.add(attr)
                        }
                    }
                }
            }

            return results
        }

        fun xmlParentField(element: PsiElement): String? {
            var elementToExplore = PsiTreeUtil.getParentOfType(element, XmlTag::class.java)

            while(elementToExplore != null && elementToExplore.name != "field") {
                elementToExplore = PsiTreeUtil.getParentOfType(elementToExplore, XmlTag::class.java)
            }

            return elementToExplore?.getAttributeValue("name")
        }

        fun referenceAxelorViews(psiFile: PsiFile): Set<MetaReference> {
            val references: MutableSet<MetaReference> = mutableSetOf()
            if(psiFile !is XmlFile) {
                return references
            }

            val rootTag: XmlTag? = psiFile.rootTag
            // For each attribute in the file, we search if it references to a view with filter()
            val tags = PsiTreeUtil.findChildrenOfAnyType(rootTag, XmlTag::class.java)

            tags.filter {
                XmlTagLang.viewReferences.contains(it.name)
            }.forEach {
                val tag: XmlParentViewReference = (XmlTagLang.viewReferences[it.name]?.constructors?.get(0)?.newInstance(it) as XmlParentViewReference)
                references.addAll(tag.getViewReferences())
            }

            return references
        }

        fun referenceAxelorActions(psiFile: PsiFile): Set<MetaReference> {
            val references: MutableSet<MetaReference> = mutableSetOf()
            if(psiFile !is XmlFile) {
                return references
            }

            val rootTag: XmlTag? = psiFile.rootTag
            val tags = PsiTreeUtil.findChildrenOfAnyType(rootTag, XmlTag::class.java)

            tags.filter {
                XmlTagLang.actionReferences.contains(it.name)
            }.forEach {
                val tag: XmlParentActionReference = (XmlTagLang.actionReferences[it.name]?.constructors?.get(0)?.newInstance(it) as XmlParentActionReference)
                references.addAll(tag.getActionReferences())
            }

            return references
        }

        fun findViewReference(project: Project, viewName: String): List<PsiElement> {
            val result: MutableList<PsiElement> = ArrayList()

            val files = FileBasedIndex.getInstance().getContainingFiles(ViewReferencesIndex.KEY, viewName, ProjectScope.getProjectScope(project))
            for (virtualFile in files) {
                val datas = FileBasedIndex.getInstance().getFileData(ViewReferencesIndex.KEY, virtualFile, project)

                datas.entries.forEach { (key, value) ->
                    val psiFile = PsiManager.getInstance(project).findFile(virtualFile)
                    val psiElement = PsiUtil.getElementAtOffset(psiFile!!, value)
                    val nameFound = (psiElement.parent as XmlAttribute).value
                    nameFound?.let {
                        if(it == viewName) {
                            result.add(psiElement)
                        }
                    }
                }
            }
            return result
        }

        fun findActionReference(project: Project, viewName: String): List<PsiElement> {
            val result: MutableList<PsiElement> = ArrayList()

            val files = FileBasedIndex.getInstance().getContainingFiles(ActionReferencesIndex.KEY, viewName, ProjectScope.getProjectScope(project))
            for (virtualFile in files) {
                val datas = FileBasedIndex.getInstance().getFileData(ActionReferencesIndex.KEY, virtualFile, project)

                datas.entries.forEach { (key, value) ->
                    val psiFile = PsiManager.getInstance(project).findFile(virtualFile)
                    val psiElement = PsiUtil.getElementAtOffset(psiFile!!, value)
                    val namesFound = (psiElement.parent as XmlAttribute).value?.split(",")
                    namesFound?.forEach {
                        if(it == viewName) {
                            result.add(psiElement)
                        }
                    }
                }
            }
            return result
        }
    }

}