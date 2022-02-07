package fr.phpierre.axelordevtools.objects.xml

import fr.phpierre.axelordevtools.objects.MetaReference

interface XmlParentViewReference {
    /**
     * A Xml Tag can target multiple view like the field component, so it could have multiple references that we group in this method.
     */
    fun getViewReferences(): List<MetaReference>
}