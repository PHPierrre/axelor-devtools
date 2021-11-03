package fr.phpierre.axelordevtools.contributor.xml

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.xml.XMLLanguage
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.ProjectScope
import com.intellij.util.ProcessingContext
import com.intellij.util.indexing.FileBasedIndex
import fr.phpierre.axelordevtools.indexes.DomainPackageIndex
import fr.phpierre.axelordevtools.indexes.SelectionNameIndex
import fr.phpierre.axelordevtools.references.xml.XmlNameReferenceContributor
import fr.phpierre.axelordevtools.util.XmlUtil
import org.jetbrains.annotations.NotNull

class SelectionCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC, PlatformPatterns.psiElement().withLanguage(XMLLanguage.INSTANCE).inside(
                XmlNameReferenceContributor.SELECTION),
            object : CompletionProvider<CompletionParameters?>() {
                override fun addCompletions(
                    parameters: @NotNull CompletionParameters,
                    context: @NotNull ProcessingContext,
                    resultSet: @NotNull CompletionResultSet
                ) {
                    val keys =
                        FileBasedIndex.getInstance().getAllKeys(SelectionNameIndex.KEY, parameters.editor.project!!)
                    for (key in keys) {
                        resultSet.addElement(LookupElementBuilder.create(key).withIcon(AllIcons.Actions.ListFiles))
                    }
                }
            }
        )
    }
}