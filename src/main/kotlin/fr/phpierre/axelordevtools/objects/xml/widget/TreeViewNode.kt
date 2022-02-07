package fr.phpierre.axelordevtools.objects.xml.widget

import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.objects.xml.AbstractWidget
import fr.phpierre.axelordevtools.objects.xml.XmlParentActionReference

class TreeViewNode(xmlTag: XmlTag) : AbstractWidget(xmlTag), XmlParentActionReference {

    fun getOnClickReference(): MutableList<MetaReference>? {
        return getMetaReferences("onClick")
    }

    override fun getActionReferences(): List<MetaReference> {
        val references: MutableList<MetaReference> = mutableListOf()
        getOnClickReference()?.let {
            references.addAll(it)
        }
        return references
    }
}