package fr.phpierre.axelordevtools.objects.xml.widget

import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.objects.xml.XmlParentActionReference

class MenuItem(xmlTag: XmlTag) : SimpleWidget(xmlTag), XmlParentActionReference {
    fun getActionReference(): List<MetaReference>? {
        return getMetaReferences("action")
    }

    override fun getActionReferences(): List<MetaReference> {
        val references: MutableList<MetaReference> = mutableListOf()
        getActionReference()?.let {
            references.addAll(it)
        }
        return references
    }
}