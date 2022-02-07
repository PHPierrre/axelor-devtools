package fr.phpierre.axelordevtools.objects.xml.action

import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.objects.xml.AbstractAction
import fr.phpierre.axelordevtools.objects.xml.XmlParentActionReference
import fr.phpierre.axelordevtools.objects.xml.XmlParentViewReference

class ActionView(xmlTag: XmlTag) : AbstractAction(xmlTag), XmlParentViewReference {

    fun getNameViewReference(): MetaReference? {
        return getMetaReference("name")
    }

    override fun getViewReferences(): List<MetaReference> {
        val references: MutableList<MetaReference> = mutableListOf()
        getNameViewReference()?.let {
            references.add(it)
        }
        return references
    }

}