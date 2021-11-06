package fr.phpierre.axelordevtools.contributor.xml

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.xml.XMLLanguage
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.XmlPatterns
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
import icons.PropertiesIcons
import org.jetbrains.annotations.NotNull

class ViewParamCompletionContributor:  CompletionContributor() {

    // <view-param ... name="xxx" ../>
    val VIEW_PARAM_NAME = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("name").withParent(XmlPatterns.psiElement().withName("view-param")))

    val params = setOf(
        "popup",
        "show-toolbar",
        "forceEdit",
        "forceTitle",
        "show-confirm",
        "showArchived",
        "details-view",
        "search-filters",
        "limit",
        "popup-save",
        "popup.maximized",
        "show-confirm",
        "reload-dotted",
        "download"
    );
    init {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement().withLanguage(XMLLanguage.INSTANCE).inside(
            VIEW_PARAM_NAME), object : CompletionProvider<CompletionParameters?>() {
            override fun addCompletions(
                parameters: @NotNull CompletionParameters,
                context: @NotNull ProcessingContext,
                resultSet: @NotNull CompletionResultSet
            ) {
               for (param in params) {
                   resultSet.addElement(LookupElementBuilder.create(param).withIcon(AllIcons.Nodes.Field));
               }
            }
        } )
    }
}