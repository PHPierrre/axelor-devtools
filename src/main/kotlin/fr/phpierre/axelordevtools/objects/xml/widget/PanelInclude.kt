package fr.phpierre.axelordevtools.objects.xml.widget

import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.objects.xml.XmlParentViewReference

class PanelInclude(xmlTag: XmlTag) : FormInclude(xmlTag), XmlParentViewReference {

    fun getViewViewReference(): MetaReference? {
        return getMetaReference("view")
    }

    override fun getViewReferences(): List<MetaReference> {
        val references: MutableList<MetaReference> = mutableListOf()
        getViewViewReference()?.let {
            references.add(it)
        }
        return references
    }

}