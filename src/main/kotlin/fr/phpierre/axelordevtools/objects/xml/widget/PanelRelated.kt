package fr.phpierre.axelordevtools.objects.xml.widget

import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.objects.xml.XmlParentActionReference
import fr.phpierre.axelordevtools.objects.xml.XmlParentViewReference

class PanelRelated(xmlTag: XmlTag) : AbstractPanel(xmlTag), XmlParentViewReference, XmlParentActionReference {

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

    fun getOnSelectActionReference(): List<MetaReference>? {
        return getMetaReferences("onSelect")
    }

    fun getOnNewActionReference(): List<MetaReference>? {
        return getMetaReferences("onNew")
    }

    fun getOnChangeActionReference(): List<MetaReference>? {
        return getMetaReferences("onChange")
    }

    fun getOnTabSelectActionReference(): List<MetaReference>? {
        return getMetaReferences("onTabSelect")
    }

    override fun getActionReferences(): List<MetaReference> {
        val references: MutableList<MetaReference> = mutableListOf()
        getOnSelectActionReference()?.let {
            references.addAll(it)
        }
        getOnNewActionReference()?.let {
            references.addAll(it)
        }
        getOnChangeActionReference()?.let {
            references.addAll(it)
        }
        getOnTabSelectActionReference()?.let {
            references.addAll(it)
        }
        return references
    }
}