package fr.phpierre.axelordevtools.contributor.xml

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.xml.XMLLanguage
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.ProjectScope
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ProcessingContext
import com.intellij.util.indexing.FileBasedIndex
import fr.phpierre.axelordevtools.icons.AxelorIcons
import fr.phpierre.axelordevtools.indexes.DomainPackageIndex
import fr.phpierre.axelordevtools.indexes.SelectionNameIndex
import fr.phpierre.axelordevtools.indexes.ViewNameIndex
import fr.phpierre.axelordevtools.references.xml.XmlNameReferenceContributor
import org.jetbrains.annotations.NotNull
import java.awt.Color

class ViewCompletionContributor : CompletionContributor() {

    init {
        extend(
                CompletionType.BASIC, PlatformPatterns.psiElement().withLanguage(XMLLanguage.INSTANCE).inside(
                XmlNameReferenceContributor.NAME_VIEW),
                object : CompletionProvider<CompletionParameters?>() {
                    override fun addCompletions(
                            parameters: @NotNull CompletionParameters,
                            context: @NotNull ProcessingContext,
                            resultSet: @NotNull CompletionResultSet
                    ) {
                        val project = parameters.editor.project!!

                        val keys = FileBasedIndex.getInstance().getAllKeys(ViewNameIndex.KEY, project)
                        for (key in keys) {
                            val virtualFiles = FileBasedIndex.getInstance().getContainingFiles(ViewNameIndex.KEY, key, ProjectScope.getProjectScope(project))
                            for (virtualFile in virtualFiles) {
                                val file: PsiFile? = PsiManager.getInstance(project).findFile(virtualFile)
                                val rootTag: XmlTag? = (file as XmlFile).rootTag

                                rootTag?.subTags?.forEach { tag ->
                                    if(tag.getAttribute("name")?.value == key) {
                                        val title: String = tag.getAttribute("title")?.value ?: ""
                                        val type = tag.name
                                        resultSet.addElement(
                                                LookupElementBuilder.create(key)
                                                        .withIcon(AxelorIcons.viewIcon)
                                                        .withTypeText(type)
                                                        .withTailText(title)

                                        )
                                    }
                                }

                            }
                        }
                    }
                }
        )
    }
}