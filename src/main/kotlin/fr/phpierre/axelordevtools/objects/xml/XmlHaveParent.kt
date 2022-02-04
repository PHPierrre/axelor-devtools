package fr.phpierre.axelordevtools.objects.xml

import fr.phpierre.axelordevtools.objects.MetaReference

interface XmlHaveParent {
    fun getReferences(): List<MetaReference>
}