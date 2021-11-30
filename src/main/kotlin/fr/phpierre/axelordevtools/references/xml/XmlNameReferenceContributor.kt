package fr.phpierre.axelordevtools.references.xml

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.*
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import com.intellij.util.ProcessingContext
import com.intellij.psi.PsiElement
import fr.phpierre.axelordevtools.references.java.JavaMethodReference
import fr.phpierre.axelordevtools.references.xml.condition.DefaultFieldCondition
import fr.phpierre.axelordevtools.references.xml.condition.DumbFieldCondition
import fr.phpierre.axelordevtools.references.xml.condition.ViewFileCondition


class XmlNameReferenceContributor : PsiReferenceContributor() {

    companion object {
        // <... model="xxx" ...>
        val MODEL_NAME = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("model"))

        // <... ref="xxxx" ...>
        val REF_DOMAIN = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("ref"))

        // <entity .. extends="xxxx" ...>
        val EXTENDS_ENTITY = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("extends").withParent(XmlPatterns.xmlTag().withName("entity")))

        val MODEL_DOMAIN = PlatformPatterns.or(MODEL_NAME, REF_DOMAIN, EXTENDS_ENTITY)

        // <field ... name="xxx" .... />
        // do not detect name that start with a dollar : <field ... name="$xxx" .... />
        val FIELD_NAME = XmlPatterns.xmlAttributeValue()
                .with(ViewFileCondition("XML_VIEW_FILE"))
                .with(DumbFieldCondition("XML_DUMMY_FIELD"))
                .with(DefaultFieldCondition("XML_DEFAULT_FIELD"))
                .withParent(XmlPatterns.xmlAttribute("name")
                .withParent(XmlPatterns.xmlTag().withName("field")))

        /**
         * <selection name="xxxx">
         *     <option ..></option>
         * </selection>
         */
        val SELECTION_NAME = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("name").withParent(XmlPatterns.psiElement().withName("selection")))

        //<...selection="xxx" ...>
        val SELECTION = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("selection"))


        /**
         * <action-method name="...">
         *     <call class="..." method="xxx"/>
         * </action-method>
         */
        val ACTION_METHOD_VIEW = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("method").withParent(XmlPatterns.psiElement().withName("call")))

        /**
        * <action-view name=".." model="...">
         *     <view type="grid" name="xxx"/>
         *     <view type="form" name="xxx"/>
         * </action-view>
         */
        val ACTION_VIEW_VIEW = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("name").withParent(XmlPatterns.psiElement().withName("view")))

        // <panel-include ... view="xxx" .. />
        val PANEL_INCLUDE_VIEW = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("view").withParent(XmlPatterns.psiElement().withName("panel-include")))

        // <... grid-view="xxx" ..>
        val GRID_VIEW_VIEW = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("grid-view"))

        // <... form-view="xxx" ..>
        val FORM_VIEW_VIEW = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("form-view"))

        // When a xml attribute value target a view name.
        val NAME_VIEW = PlatformPatterns.or(ACTION_VIEW_VIEW, PANEL_INCLUDE_VIEW, GRID_VIEW_VIEW, FORM_VIEW_VIEW)

        val AXELOR_VIEW = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                return arrayOf(AxelorViewNameReference(element))
            }
        }

        val AXELOR_DOMAIN = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                return arrayOf(AxelorDomainReference(element))
            }
        }

        val AXELOR_FIELD = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                return arrayOf(XmlAttributeNameReference(element))
            }
        }

        val AXELOR_SELECTION = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                return arrayOf(XmlAttributeSelectionReference(element))
            }
        }

        val AXELOR_JAVA_METHOD = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                return arrayOf(JavaMethodReference(element))
            }
        }
    }

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(NAME_VIEW, AXELOR_VIEW)
        registrar.registerReferenceProvider(MODEL_DOMAIN, AXELOR_DOMAIN)
        registrar.registerReferenceProvider(FIELD_NAME, AXELOR_FIELD)
        registrar.registerReferenceProvider(SELECTION, AXELOR_SELECTION)
        registrar.registerReferenceProvider(ACTION_METHOD_VIEW, AXELOR_JAVA_METHOD)
    }
}