package fr.phpierre.axelordevtools.contributor.xml

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.xml.XMLLanguage
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ProcessingContext
import fr.phpierre.axelordevtools.icons.AxelorIcons
import fr.phpierre.axelordevtools.references.xml.XmlNameReferenceContributor
import fr.phpierre.axelordevtools.util.PsiElementUtil
import fr.phpierre.axelordevtools.util.XmlUtil
import org.jetbrains.annotations.NotNull
import java.awt.Color


class FieldCompletionContributor : CompletionContributor() {

    companion object {

        val relationColor = Color(62, 207, 142)

        val relationTag = setOf<String>("one-to-one", "one-to-many", "many-to-one", "many-to-many")


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
                    val project = parameters.editor.project!!
                    val autoCompletion = PsiElementUtil.getAutoCompleteValue(parameters.position)

                    modelName?.let {
                        val fields: Set<PsiElement> = XmlUtil.findFieldInModelNameAndParents(project, autoCompletion, modelName, XmlUtil.FieldSearch.ALL)

                        for (field in fields) {
                            (field as XmlAttribute).value?.let {
                                val parent = field.parent as XmlTag

                                var fieldName = field.value!!
                                // If it's a relational field
                                // Seem like that withPrefixMatcher does not work (l 91).
                                if(autoCompletion.contains(".")) {
                                    val prefix = autoCompletion.substring(0, autoCompletion.lastIndexOf(".")) + "."
                                    fieldName = prefix + fieldName
                                    addDefaultFields(prefix, resultSet)
                                }

                                // Create an item with the field name, an icon and a type
                                var item = LookupElementBuilder.create(fieldName).withIcon(AxelorIcons.fieldIcon).withTypeText(formatType(parent))

                                // Add information title
                                parent.getAttribute("title")?.let { titleAttr ->
                                    titleAttr.value?.let { titleText -> item = item.withTailText(" $titleText") }
                                }

                                // Add color if it's a relation
                                if(relationTag.contains(parent.name)) {
                                    item = item.withItemTextForeground(relationColor)
                                }

                                // Does not work for unknown reason
                                // If it's a relational field
//                                if(autoCompletion.contains(".")) {
//                                    val prefix = autoCompletion.substring(0, autoCompletion.lastIndexOf(".")) + "."
//                                    resultSet.withPrefixMatcher(prefix).addElement(item)
//                                } else {
//                                    resultSet.addElement(item)
//                                }
                                resultSet.addElement(item)
                            }
                        }

                        addDefaultFields("", resultSet)
                    }
                }
            }
        )
    }

    private fun formatType(xmlField: XmlTag): String {
        val xmlType = xmlField.name
        var relationType = ""
        var javaType = ""

        if(relationTag.contains(xmlType)) {
            extractJavaType(xmlField)?.let {
                javaType = it
            }
        }

        if (xmlType == "many-to-many") {
            javaType = "List<$javaType>"
            relationType = " (m2m)"
        } else if (xmlType == "one-to-many") {
            javaType = "List<$javaType>"
            relationType = " (o2m)"
        } else if (xmlType == "many-to-one") {
            relationType = " (m2o)"
        } else if(xmlType == "one-to-one") {
            relationType = " (o2o)"
        } else {
            relationType = xmlField.name
        }

        return "$javaType$relationType"
    }

    private fun extractJavaType(xmlField: XmlTag): String? {
        return xmlField.getAttribute("ref")?.value?.substringAfterLast(".")
    }

    private fun addDefaultFields(prefix: String, resultSet: CompletionResultSet) {
        for(field in defaultColumns) {
            resultSet.addElement(LookupElementBuilder.create(prefix + field)
                .withIcon(AxelorIcons.fieldIcon))
        }
    }

}