package fr.phpierre.axelordevtools.objects.xml

import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.objects.MetaReference

abstract class AbstractMetaView(xmlTag: XmlTag) : AbstractXML(xmlTag), XmlParentViewReference {

    override fun getViewReferences(): List<MetaReference> {
        val references: MutableList<MetaReference> = mutableListOf()
        getNameReference()?.let {
            references.add(it)
        }
        return references
    }

    fun getNameReference(): MetaReference? {
        return getMetaReference("name")
    }
}