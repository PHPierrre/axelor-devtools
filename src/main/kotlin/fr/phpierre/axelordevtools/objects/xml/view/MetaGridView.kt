package fr.phpierre.axelordevtools.objects.xml.view

import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.objects.xml.AbstractMetaView
import fr.phpierre.axelordevtools.objects.xml.XmlParentActionReference

class MetaGridView(xmlTag: XmlTag) : AbstractMetaView(xmlTag), XmlParentActionReference {
    fun getOnNewActionReference(): List<MetaReference>? {
        return getMetaReferences("onNew")
    }

    override fun getActionReferences(): List<MetaReference> {
        val references: MutableList<MetaReference> = mutableListOf()
        getOnNewActionReference()?.let {
            references.addAll(it)
        }
        return references
    }
}