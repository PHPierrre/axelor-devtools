package fr.phpierre.axelordevtools.objects.dom

import com.intellij.psi.xml.XmlAttribute
import com.intellij.util.xml.*

interface DomainModels : DomElement{

    @SubTag("module")
    fun getEntityModule(): Module

    @SubTag("entity")
    fun getEntity(): Entity

    @SubTagList("enum")
    fun getEnums(): List<Enum>
}

interface Module : DomElement {

    fun getName(): String

    fun getPackage(): String
}

interface Entity : DomElement {
    fun getEnums(): List<EntityEnum>

    /*@SubTagsList(value = ["integer", "long", "string", "boolean", "decimal", "datetime", "date", "time", "binary"])
    fun getProperties(): List<EntityProperties>*/

    // TODO : do r2m
}

interface EntityProperties : DomElement {

}

interface EntityEnum : DomElement {
    fun getName(): GenericAttributeValue<String>
    fun getRef(): GenericAttributeValue<String> // to FQN ?
}

interface Enum : DomElement {
    fun getName(): GenericAttributeValue<String>
    fun getItems(): List<EnumItem>
}

interface EnumItem : DomElement {
    fun getName(): GenericAttributeValue<String>

    fun getValue(): GenericAttributeValue<String>
}