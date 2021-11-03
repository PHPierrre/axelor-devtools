package fr.phpierre.axelordevtools.references.xml

import com.intellij.openapi.rd.createLifetime
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.*
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import com.intellij.util.ProcessingContext
import com.intellij.psi.PsiFile

import com.intellij.psi.PsiElement
import com.intellij.patterns.PatternCondition
import com.intellij.psi.impl.source.resolve.reference.PsiReferenceUtil
import com.intellij.psi.util.PsiUtilBase
import org.jetbrains.annotations.NotNull


class XmlNameReferenceContributor : PsiReferenceContributor() {

    companion object {
        // <... model="xxx" ...>
        val MODEL_NAME = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("model"))

        // <... ref="xxxx" ...>
        val REF_DOMAIN = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("ref"))

        // <field ... name="xxx" .... />
        // do not detect name that start with a dollar : <field ... name="$xxx" .... />
        val FIELD_NAME = XmlPatterns.xmlAttributeValue().with(DumbFieldCondition("XML_FIELD")).withParent(XmlPatterns.xmlAttribute("name").withParent(XmlPatterns.xmlTag().withName("field")))

        /**
         * <selection name="xxxx">
         *     <option ..></option>
         * </selection>
         */
        val SELECTION_NAME = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("name").withParent(XmlPatterns.psiElement().withName("selection")))

        //<...selection="xxx" ...>
        val SELECTION = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("selection"))

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
    }

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(NAME_VIEW, AXELOR_VIEW)
        registrar.registerReferenceProvider(MODEL_NAME, AXELOR_DOMAIN)
        registrar.registerReferenceProvider(REF_DOMAIN, AXELOR_DOMAIN)

        registrar.registerReferenceProvider(FIELD_NAME,
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                        if(element !is XmlAttributeValueImpl)
                            return PsiReference.EMPTY_ARRAY

                        return arrayOf(XmlAttributeNameReference(element))
                    }
                })
    }
}