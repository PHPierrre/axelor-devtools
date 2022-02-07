package fr.phpierre.axelordevtools.objects.xml.action

import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.objects.xml.AbstractAction
import fr.phpierre.axelordevtools.objects.xml.XmlParentActionReference

class Action(xmlTag: XmlTag) : AbstractAction(xmlTag), XmlParentActionReference {
    fun getActionName(): MetaReference? {
        return getMetaReference("name")
    }

    override fun getActionReferences(): List<MetaReference> {
        val references: MutableList<MetaReference> = mutableListOf()
        getActionName()?.let {
            references.add(it)
        }
        return references
    }
}