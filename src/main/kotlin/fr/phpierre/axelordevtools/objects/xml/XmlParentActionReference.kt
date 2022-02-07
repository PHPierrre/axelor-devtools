package fr.phpierre.axelordevtools.objects.xml

import fr.phpierre.axelordevtools.objects.MetaReference

interface XmlParentActionReference {
    fun getActionReferences(): List<MetaReference>
}