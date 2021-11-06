package fr.phpierre.axelordevtools.contributor.xml

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.xml.XMLLanguage
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import com.intellij.util.indexing.FileBasedIndex
import fr.phpierre.axelordevtools.indexes.DomainPackageIndex
import fr.phpierre.axelordevtools.indexes.SelectionNameIndex
import fr.phpierre.axelordevtools.references.xml.XmlNameReferenceContributor
import org.jetbrains.annotations.NotNull

class ModelCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC, PlatformPatterns.psiElement().withLanguage(XMLLanguage.INSTANCE).inside(
                XmlNameReferenceContributor.MODEL_DOMAIN),
            object : CompletionProvider<CompletionParameters?>() {
                override fun addCompletions(
                    parameters: @NotNull CompletionParameters,
                    context: @NotNull ProcessingContext,
                    resultSet: @NotNull CompletionResultSet
                ) {
                    val keys =
                        FileBasedIndex.getInstance().getAllKeys(DomainPackageIndex.KEY, parameters.editor.project!!)
                    for (key in keys) {
                        resultSet.addElement(LookupElementBuilder.create(key).withIcon(AllIcons.Nodes.Class))
                    }
                }
            }
        )
    }
}