package fr.phpierre.axelordevtools.objects.xml

import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.objects.MetaReference
import javax.xml.bind.annotation.XmlAttribute

abstract class AbstractMetaView(xmlTag: XmlTag) : AbstractXML(xmlTag), XmlHaveParent {

    override fun getReferences(): List<MetaReference> {
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