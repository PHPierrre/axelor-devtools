package fr.phpierre.axelordevtools.contributor.xml

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.xml.XMLLanguage
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.source.xml.XmlAttributeImpl
import com.intellij.psi.search.ProjectScope
import com.intellij.util.ProcessingContext
import com.intellij.util.indexing.FileBasedIndex
import fr.phpierre.axelordevtools.indexes.DomainPackageIndex
import fr.phpierre.axelordevtools.references.xml.XmlNameReferenceContributor
import fr.phpierre.axelordevtools.util.XmlUtil
import org.jetbrains.annotations.NotNull


class FieldCompletionContributor : CompletionContributor() {

    companion object {
        // These columns are present in every table.
        val defaultColumns = setOf(
            "id",
            "UUID",
            "archived",
            "externalCode",
            "externalId",
            "version",
            "createdOn",
            "updatedOn",
            "attrs",
            "createdBy",
            "updatedBy")
    }

    init {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement().withLanguage(XMLLanguage.INSTANCE).inside(XmlNameReferenceContributor.FIELD_NAME),
            object : CompletionProvider<CompletionParameters?>() {
                override fun addCompletions(
                    parameters: @NotNull CompletionParameters,
                    context: @NotNull ProcessingContext,
                    resultSet: @NotNull CompletionResultSet
                ) {
                    val modelName = XmlUtil.xmlViewRootModelName(parameters.position)
                    modelName?.let {
                        val virtualFiles = FileBasedIndex.getInstance().getContainingFiles(DomainPackageIndex.KEY, modelName, ProjectScope.getProjectScope(parameters.editor.project!!))
                        for (virtualFile in virtualFiles) {
                            val psiFile: PsiFile? = PsiManager.getInstance(parameters.editor.project!!).findFile(virtualFile)
                            val fields: MutableSet<PsiElement> = XmlUtil.getFieldsFromDomain(psiFile!!)

                            for (field in fields) {
                                (field as XmlAttributeImpl).value?.let {
                                    resultSet.addElement(
                                        LookupElementBuilder.create(it)
                                            .withPsiElement(field)
                                                .withIcon(AllIcons.Nodes.Field)
                                    )
                                }
                            }

                            addDefaultFields(resultSet)
                        }
                    }
                }
            }
        )
    }

    private fun addDefaultFields(resultSet: CompletionResultSet) {
        for(field in defaultColumns) {
            resultSet.addElement(LookupElementBuilder.create(field)
                .withIcon(AllIcons.Nodes.Field))
        }
    }
}