package fr.phpierre.axelordevtools.objects.xml.widget

import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.objects.MetaReference
import fr.phpierre.axelordevtools.objects.xml.XmlParentActionReference
import fr.phpierre.axelordevtools.objects.xml.XmlParentSelectionReference
import fr.phpierre.axelordevtools.objects.xml.XmlParentViewReference

class Field(xmlTag: XmlTag) : SimpleWidget(xmlTag), XmlParentViewReference, XmlParentActionReference, XmlParentSelectionReference {

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

    fun getOnChangeReferences(): List<MetaReference>? {
        return getMetaReferences("onChange")
    }

    fun getOnSelectReferences(): List<MetaReference>? {
        return getMetaReferences("onSelect")
    }

    override fun getActionReferences(): List<MetaReference> {
        val references: MutableList<MetaReference> = mutableListOf()
        getOnChangeReferences()?.let {
            references.addAll(it)
        }
        getOnSelectReferences()?.let{
            references.addAll(it)
        }
        return references
    }

    override fun getSelectionReferences(): MetaReference? {
        return getMetaReference("selection")
    }


}