package fr.phpierre.axelordevtools.objects.xml

import com.intellij.psi.xml.XmlTag
import com.intellij.refactoring.suggested.startOffset
import fr.phpierre.axelordevtools.objects.MetaReference
import javax.xml.bind.annotation.XmlAttribute

abstract class AbstractXML(val xmlTag: XmlTag) {

    fun getMetaReference(attributeName: String): MetaReference? {
        xmlTag.getAttribute(attributeName)?.let {
            if(!it.value.isNullOrBlank()) return MetaReference(it.value!!, it.startOffset)
        }
        return null
    }
}