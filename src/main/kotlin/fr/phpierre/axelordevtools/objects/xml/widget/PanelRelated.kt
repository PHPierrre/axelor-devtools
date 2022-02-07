package fr.phpierre.axelordevtools.objects.xml.widget

import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.objects.xml.XmlParentViewReference

class PanelRelated(xmlTag: XmlTag) : AbstractPanel(xmlTag), XmlParentViewReference {

    fun getFormViewReference(): MetaReference? {
        return getMetaReference("form-view")
    }

    fun getGridViewReference(): MetaReference? {
        return getMetaReference("grid-view")
    }

    override fun getViewReferences(): List<MetaReference> {
        val references: MutableList<MetaReference> = mutableListOf()
        getGridViewReference()?.let {
            references.add(it)
        }
        getFormViewReference()?.let {
            references.add(it)
        }
        return references
    }
}