package fr.phpierre.axelordevtools.objects.xml.widget

import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag
import com.intellij.refactoring.suggested.startOffset
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.objects.xml.XmlHaveParent

class Field(xmlTag: XmlTag) : SimpleWidget(xmlTag), XmlHaveParent {

    fun getFormViewReference(): MetaReference? {
        return getMetaReference("form-view")
    }

    fun getGridViewReference(): MetaReference? {
        return getMetaReference("grid-view")
    }

    override fun getReferences(): List<MetaReference> {
        val references: MutableList<MetaReference> = mutableListOf()
        getGridViewReference()?.let {
            references.add(it)
        }
        getFormViewReference()?.let {
            references.add(it)
        }
        return references
    }


}