package fr.phpierre.axelordevtools.contributor.xml

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.xml.XMLLanguage
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.XmlPatterns
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull

class DomainCompletionContributor :  CompletionContributor() {

    // <entity ... strategy="xxx" ..>
    val ENTITY_STRATEGY = XmlPatterns.xmlAttributeValue().withParent(
        XmlPatterns.xmlAttribute("strategy").withParent(
            XmlPatterns.psiElement().withName("entity")))

    val strategies = setOf("SINGLE", "JOINED", "CLASS");

    init {
        /*extend(
            CompletionType.BASIC, PlatformPatterns.psiElement().withLanguage(XMLLanguage.INSTANCE).inside(
                ENTITY_STRATEGY), object : CompletionProvider<CompletionParameters?>() {
            override fun addCompletions(
                parameters: @NotNull CompletionParameters,
                context: @NotNull ProcessingContext,
                resultSet: @NotNull CompletionResultSet
            ) {
                for (strategy in strategies) {
                    resultSet.addElement(LookupElementBuilder.create(strategy).withIcon(AllIcons.Nodes.Field));
                }
            }
        } )*/
    }
}