package fr.phpierre.axelordevtools.objects.xml

import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag
import com.intellij.refactoring.suggested.startOffset
import fr.phpierre.axelordevtools.objects.MetaReference
import javax.xml.bind.annotation.XmlAttribute

abstract class AbstractXML(val xmlTag: XmlTag) {

    /**
     * Find the position of the attribute name in an XML file.
     * @param attributeName XML attribute name.
     * @return A MetaReference which target the attribute name in the XmlTag.
     */
    fun getMetaReference(attributeName: String): MetaReference? {
        xmlTag.getAttribute(attributeName)?.let {
            if(!it.value.isNullOrBlank()) return MetaReference(it.value!!, it.startOffset)
        }
        return null
    }

    /**
     * Find the position of the attribute name in an XML file.
     * @param attributeName XML attribute name.
     * @return A list of MetaReference which target the attribute name in the XmlTag.
     */
    fun getMetaReferences(attributeName: String): MutableList<MetaReference>? {
        xmlTag.getAttribute(attributeName)?.let { attribute ->
            val mutableList: MutableList<MetaReference> = mutableListOf()
            if(!attribute.value.isNullOrBlank()) {
                attribute.value?.split(",")?.forEach { actionName ->
                    mutableList.add(MetaReference(actionName, attribute.startOffset))
                }
            }

            return mutableList
        }
        return null
    }
}