package fr.phpierre.axelordevtools.contributor

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlAttribute
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase
import fr.phpierre.axelordevtools.contributor.xml.FieldCompletionContributor


class FieldCompletionCodeInsightTest : LightJavaCodeInsightFixtureTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/testData/fake-axelor-module/src/main"
    }

    fun testCompletionField() {
        myFixture.configureByFiles("resources/views/View.xml", "resources/domains/View.xml")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings: MutableList<String> = myFixture.lookupElementStrings!!
        assertNotNull(lookupElementStrings)
        assertSameElements(lookupElementStrings, "city", "name", "description", "viewType", "isActive",
                "id", "UUID", "archived", "externalCode", "externalId", "version", "createdOn", "updatedOn", "attrs", "createdBy", "updatedBy")
    }

    fun testCompletionModel() {
        myFixture.configureByFiles("resources/views/ViewModel.xml", "resources/domains/View.xml")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings: MutableList<String> = myFixture.lookupElementStrings!!
        assertNotNull(lookupElementStrings)
        assertSameElements(lookupElementStrings, "fr.phpierre.faxeaxelormodule.View")
    }

    fun testCompletionSelection() {
        myFixture.configureByFiles("resources/views/ViewSelection.xml", "resources/views/Selects.xml")
        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings: MutableList<String> = myFixture.lookupElementStrings!!
        assertNotNull(lookupElementStrings)
        assertSameElements(lookupElementStrings, "view.type.select")
    }

    fun testReferenceField() {
        val referenceAtCaret =
            myFixture.getReferenceAtCaretPositionWithAssertion("resources/views/ViewReference.xml", "resources/domains/View.xml")
        val resolvedSimpleProperty: XmlAttribute = assertInstanceOf(
            referenceAtCaret.resolve(),
            XmlAttribute::class.java
        )
        assertEquals("city", resolvedSimpleProperty.value)
    }
}