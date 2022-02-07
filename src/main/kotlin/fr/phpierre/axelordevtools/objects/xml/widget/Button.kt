package fr.phpierre.axelordevtools.objects.xml.widget

import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.objects.xml.XmlParentActionReference

class Button(xmlTag: XmlTag) : SimpleWidget(xmlTag), XmlParentActionReference {
    fun getOnClickReferences(): List<MetaReference>? {
        return getMetaReferences("onClick")
    }

    override fun getActionReferences(): List<MetaReference> {
        val references: MutableList<MetaReference> = mutableListOf()
        getOnClickReferences()?.let {
            references.addAll(it)
        }
        return references
    }
}