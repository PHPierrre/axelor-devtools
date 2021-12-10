package fr.phpierre.axelordevtools.contributor.xml

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.xml.XMLLanguage
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.XmlPatterns
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.NotNull

class WidgetCompletionContributor: CompletionContributor() {
    // <field ... widget="xxx" ..>
    val FIELD_WIDGET = XmlPatterns.xmlAttributeValue().withParent(
        XmlPatterns.xmlAttribute("widget").withParent(
            XmlPatterns.psiElement().withName("field")))

    val widgets = setOf(
        "email",
        "url",
        "phone",
        "password",
        "Integer",
        "Decimal",
        "date",
        "time",
        "date-time",
        "boolean",
        "boolean-select",
        "boolean-radio",
        "boolean-switch",
        "Text",
        "binary",
        "image",
        "NavSelect",
        "RadioSelect",
        "CheckboxSelect",
        "InlineCheckbox",
        "ImageSelect",
        "MultiSelect",
        "SingleSelect",
        "TagSelect",
        "RefSelect",
        "Progress",
        "code-editor",
        "html",
        "binary-link",
        "json-ref-select",
        "toggle",
        "duration"
    )

    init {
        extend(
            CompletionType.BASIC, PlatformPatterns.psiElement().withLanguage(XMLLanguage.INSTANCE).inside(
                FIELD_WIDGET), object : CompletionProvider<CompletionParameters?>() {
            override fun addCompletions(
                parameters: @NotNull CompletionParameters,
                context: @NotNull ProcessingContext,
                resultSet: @NotNull CompletionResultSet
            ) {
                for (widget in widgets) {
                    resultSet.addElement(LookupElementBuilder.create(widget).withIcon(AllIcons.Nodes.Field))
                }
            }
        } )
    }
}