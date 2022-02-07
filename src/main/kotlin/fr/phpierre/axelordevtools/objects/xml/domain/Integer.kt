package fr.phpierre.axelordevtools.objects.xml.domain

import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.objects.xml.AbstractXML
import fr.phpierre.axelordevtools.objects.xml.XmlParentSelectionReference

class Integer(xmlTag: XmlTag) : AbstractXML(xmlTag), XmlParentSelectionReference {
    fun getSelection(): MetaReference? {
        return getMetaReference("selection")
    }

    override fun getSelectionReferences(): MetaReference? {
        return getSelection()
    }
}