package fr.phpierre.axelordevtools.objects.xml

import fr.phpierre.axelordevtools.objects.MetaReference

interface XmlParentSelectionReference {
    fun getSelectionReferences(): MetaReference?
}