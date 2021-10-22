package fr.phpierre.axelordevtools.references.xml

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.XmlAttributeValuePattern
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.*
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import com.intellij.util.ProcessingContext


class XmlNameReferenceContributor : PsiReferenceContributor() {

    val GRID_NAME = XmlPatterns.psiElement().withName("name").withParent(XmlPatterns.psiElement().withName("form"));

    //val FORM_NAME = XmlPatterns.psiElement().withName("name").withParent(XmlPatterns.psiElement().withName("grid"));
    val FORM_NAME = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("name").withParent(XmlPatterns.psiElement().withName("form")));

    val FIELD_NAME = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("name").withParent(XmlPatterns.xmlTag().withName("field")));

    companion object {
        val MODEL_NAME = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("model").withParent(XmlPatterns.psiElement().withName("form")));

        val MODULE_PACKAGE = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("package").withParent(XmlPatterns.psiElement().withName("module")));
        val ENTITY_NAME = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("name").withParent(XmlPatterns.psiElement().withName("entity")));
    }

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        println(registrar)

//        registrar.registerReferenceProvider(FORM_NAME,
//                object : PsiReferenceProvider() {
//                    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
//                        println("form")
//                        //println(element.firstChild.nextSibling is XmlAttributeValue)
//                        return PsiReference.EMPTY_ARRAY
//                    }
//                });

        registrar.registerReferenceProvider(FIELD_NAME,
                object : PsiReferenceProvider() {
                    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                        if(element !is XmlAttributeValueImpl)
                            return PsiReference.EMPTY_ARRAY;

                        val range = TextRange(1, element.text.length - 1)
                        return arrayOf(XmlAttributeNameReference(element as XmlAttributeValueImpl, range))
                    }
                });
    }
}