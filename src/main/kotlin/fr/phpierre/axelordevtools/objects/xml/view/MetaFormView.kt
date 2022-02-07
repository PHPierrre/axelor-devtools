package fr.phpierre.axelordevtools.objects.xml.view

import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.objects.xml.AbstractMetaView
import fr.phpierre.axelordevtools.objects.xml.XmlParentActionReference

class MetaFormView(xmlTag: XmlTag) : AbstractMetaView(xmlTag), XmlParentActionReference {

    fun getOnLoadActionReference(): List<MetaReference>? {
        return getMetaReferences("onLoad")
    }

    fun getOnNewActionReference(): List<MetaReference>? {
        return getMetaReferences("onNew")
    }

    fun getOnSaveActionReference(): List<MetaReference>? {
        return getMetaReferences("onSave")
    }

    override fun getActionReferences(): List<MetaReference> {
        val references: MutableList<MetaReference> = mutableListOf()
        getOnLoadActionReference()?.let {
            references.addAll(it)
        }
        getOnNewActionReference()?.let {
            references.addAll(it)
        }
        getOnSaveActionReference()?.let {
            references.addAll(it)
        }
        return references
    }

}