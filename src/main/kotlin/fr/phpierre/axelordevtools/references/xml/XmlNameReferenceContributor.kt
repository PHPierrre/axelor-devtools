package fr.phpierre.axelordevtools.references.xml

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.*
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import com.intellij.util.ProcessingContext

class XmlNameReferenceContributor : PsiReferenceContributor() {

    companion object {
        val MODEL_NAME = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("model"))

        val FIELD_NAME = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("name").withParent(XmlPatterns.xmlTag().withName("field")))

        /**
         * <action-view name=".." model="...">
         *     <view type="grid" name="xxx"/>
         *     <view type="form" name="xxx"/>
         * </action-view>
         */
        val ACTION_VIEW_VIEW = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("name").withParent(XmlPatterns.psiElement().withName("view")))
        val PANEL_INCLUDE_VIEW = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("view").withParent(XmlPatterns.psiElement().withName("panel-include")))
        val GRID_VIEW_VIEW = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("grid-view"))
        val FORM_VIEW_VIEW = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("form-view"))

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