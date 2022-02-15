package fr.phpierre.axelordevtools.objects.xml.action

import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.objects.xml.AbstractAction
import fr.phpierre.axelordevtools.objects.xml.XmlParentActionReference

class ActionValidate(xmlTag: XmlTag) : AbstractAction(xmlTag), XmlParentActionReference {

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