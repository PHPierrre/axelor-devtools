package fr.phpierre.axelordevtools.objects.xml.widget

import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.objects.xml.XmlParentActionReference
import fr.phpierre.axelordevtools.objects.xml.XmlParentViewReference

class MenuItem(xmlTag: XmlTag) : SimpleWidget(xmlTag), XmlParentActionReference, XmlParentViewReference {
    fun getActionReference(): List<MetaReference>? {
        return getMetaReferences("action")
    }

    fun getNameReference(): List<MetaReference>? {
        return getMetaReferences("name")
    }

    override fun getActionReferences(): List<MetaReference> {
        val references: MutableList<MetaReference> = mutableListOf()
        getActionReference()?.let {
            references.addAll(it)
        }
        return references
    }

    override fun getViewReferences(): List<MetaReference> {
        val references: MutableList<MetaReference> = mutableListOf()
        getNameReference()?.let {
            references.addAll(it)
        }
        return references
    }
}