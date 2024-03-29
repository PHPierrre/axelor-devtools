package fr.phpierre.axelordevtools.references.xml

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.*
import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl
import com.intellij.util.ProcessingContext
import fr.phpierre.axelordevtools.references.java.JavaMethodReference
import fr.phpierre.axelordevtools.references.xml.condition.DefaultFieldCondition
import fr.phpierre.axelordevtools.references.xml.condition.DumbFieldCondition
import fr.phpierre.axelordevtools.references.xml.condition.ViewFileCondition


class XmlNameReferenceContributor : PsiReferenceContributor() {

    companion object {

        // <form|grid name="xxxxx" ....>
        val VIEW_REFERENCE = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("name").withParent(XmlPatterns.xmlTag().withName("grid", "form", "")))

        // <action-view,action-attrs,action-record,action-method ...>
        val ACTIONS_NAME = XmlPatterns.xmlAttributeValue()
            .withParent(XmlPatterns.xmlAttribute("name")
            .withParent(XmlPatterns.xmlTag().withName("action-view", "action-attrs", "action-record", "action-method",
            "action-script", "action-validate", "action-condition", "action-group", "action-import", "action-export", "action-ws")))

        val PARENT_MENUITEM = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("parent").withParent(XmlPatterns.xmlTag().withName("menuitem")))

        // <... model="xxx" ...>
        val MODEL_NAME = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("model"))

        // <... ref="xxxx" ...>
        val REF_DOMAIN = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("ref").andNot(XmlPatterns.psiElement().withParent(XmlPatterns.xmlTag().withName("enum"))))

        // <entity .. extends="xxxx" ...>
        val EXTENDS_ENTITY = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("extends").withParent(XmlPatterns.xmlTag().withName("entity")))

        val MODEL_DOMAIN = PlatformPatterns.or(MODEL_NAME, REF_DOMAIN, EXTENDS_ENTITY)


        // <panel-related field="xxxxx">
        val PANEL_RELATED_VIEW = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("field").withParent(XmlPatterns.psiElement().withName("panel-related")))

        // <field ... name="xxx" .... />
        // do not detect name that start with a dollar : <field ... name="$xxx" .... />
        // do not detect fields inside <editor>
        val FIELD_NAME = XmlPatterns.xmlAttributeValue()
            .with(ViewFileCondition("XML_VIEW_FILE"))
            .with(DumbFieldCondition("XML_DUMMY_FIELD"))
            .andNot(XmlPatterns.psiElement().withAncestor(15, XmlPatterns.xmlTag().withName("editor")))
            .with(DefaultFieldCondition("XML_DEFAULT_FIELD"))
            .withParent(XmlPatterns.xmlAttribute("name")
            .withParent(XmlPatterns.xmlTag().withName("field")))

        val FIELD_NAME_IN_EDITOR = XmlPatterns.xmlAttributeValue()
            .with(ViewFileCondition("XML_VIEW_FILE"))
            .with(DumbFieldCondition("XML_DUMMY_FIELD"))
            .withAncestor(15, XmlPatterns.xmlTag().withName("editor"))
            .with(DefaultFieldCondition("XML_DEFAULT_FIELD"))
            .withParent(XmlPatterns.xmlAttribute("name")
            .withParent(XmlPatterns.xmlTag().withName("field")))
        /**
         * <selection name="xxxx">
         *     <option ..></option>
         * </selection>
         */
        val SELECTION_NAME = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("name").withParent(XmlPatterns.psiElement().withName("selection")))

        //<...selection="xxx" ...>
        val SELECTION = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("selection"))

        //<enum name="..." ref="xxx" />
        val ENUM_REF = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("ref").withParent(XmlPatterns.psiElement().withName("enum")))

        /**
         * <enum name="...">
         *     <item ...>
         *     <item ...>
         * </enum>
         */
        val ENUM_NAME = XmlPatterns.xmlAttributeValue()
                .andNot(XmlPatterns.psiElement().withAncestor(2, XmlPatterns.xmlAttribute().withName("ref")))
                .withParent(XmlPatterns.xmlAttribute("ref")
                .withParent(XmlPatterns.psiElement().withName("enum")))
        /**
         * <action-method name="...">
         *     <call class="..." method="xxx"/>
         * </action-method>
         */
        val ACTION_METHOD_VIEW = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("method").withParent(XmlPatterns.psiElement().withName("call")))

        /**
        * <action-view name=".." model="...">
         *     <view type="grid" name="xxx"/>
         *     <view type="form" name="xxx"/>
         * </action-view>
         */
        val ACTION_VIEW_VIEW = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("name").withParent(XmlPatterns.psiElement().withName("view")))

        // <panel-include ... view="xxx" .. />
        val PANEL_INCLUDE_VIEW = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("view").withParent(XmlPatterns.psiElement().withName("panel-include")))

        // <panel-dashlet ... action="xxx" .. />
        val PANEL_DASHLET_ACTION = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("action").withParent(XmlPatterns.psiElement().withName("panel-dashlet")))

        // <... grid-view="xxx" ..>
        val GRID_VIEW_VIEW = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("grid-view"))

        // <... form-view="xxx" ..>
        val FORM_VIEW_VIEW = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("form-view"))

        // When a xml attribute value target a view name.
        val NAME_VIEW = PlatformPatterns.or(ACTION_VIEW_VIEW, PANEL_INCLUDE_VIEW, GRID_VIEW_VIEW, FORM_VIEW_VIEW)


        val ACTION_ACTION = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("name").withParent(XmlPatterns.psiElement().withName("action")))

        val ON_CLICK_ACTION = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("onClick"))

        val ON_CHANGE_ACTION = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("onChange"))

        val ON_NEW_ACTION = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("onNew"))

        val ON_EDIT_ACTION = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("onEdit"))

        val ON_SAVE_ACTION = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("onSave"))

        val ON_DELETE_ACTION = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("onDelete"))

        val ON_SELECT_ACTION = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("onSelect"))

        val ON_LOAD_ACTION = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("onLoad"))

        val ON_TAB_SELECT_ACTION = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("onTabSelect"))

        val ITEM_ACTION = XmlPatterns.xmlAttributeValue().withParent(XmlPatterns.xmlAttribute("action").withParent(XmlPatterns.psiElement().withName("item", "menuitem")))

        val ACTION_VALIDATE_ACTION = XmlPatterns.xmlAttributeValue()
                .withParent(XmlPatterns.xmlAttribute("action")
                .withParent(XmlPatterns.psiElement().withName("error", "alert", "info", "notify")
                        .withParent(XmlPatterns.xmlTag().withName("action-validate"))))

        val ACTION_VIEW = PlatformPatterns.or(ACTION_ACTION, ON_CLICK_ACTION, ON_CHANGE_ACTION, ON_NEW_ACTION, ON_EDIT_ACTION,
                ON_SAVE_ACTION, ON_DELETE_ACTION, ON_SELECT_ACTION, ON_LOAD_ACTION, ON_TAB_SELECT_ACTION, ITEM_ACTION, ACTION_VALIDATE_ACTION)

        val AXELOR_VIEW = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                return arrayOf(AxelorViewNameReference(element))
            }
        }

        val AXELOR_DOMAIN = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                return arrayOf(AxelorDomainReference(element))
            }
        }

        val AXELOR_ENUM = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                return arrayOf(AxelorEnumReference(element))
            }
        }

        val AXELOR_FIELD = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                return arrayOf(XmlFieldNameReference(element))
            }
        }

        val AXELOR_FIELD_IN_EDITOR = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                return arrayOf(XmlEditorFieldNameReference(element))
            }
        }

        val AXELOR_SELECTION = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                return arrayOf(XmlAttributeSelectionReference(element))
            }
        }

        val AXELOR_JAVA_METHOD = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                return arrayOf(JavaMethodReference(element))
            }
        }

        val AXELOR_ACTION = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                val results = mutableListOf<PsiReference>()

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                var oldIndex = 1
                var index: Int = element.value.indexOf(",")
                while (index >= 0) {
                    results.add(ActionViewMethodReference(element, TextRange(oldIndex ,index +1)))
                    oldIndex = index + 2
                    index = element.value.indexOf(",", index + 1)
                }
                results.add(ActionViewMethodReference(element, TextRange(oldIndex , element.text.length -1)))

                return results.toTypedArray()
            }
        }

        val AXELOR_VIEW_REFERENCE = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                return arrayOf(AxelorViewReference(element))
            }
        }

        val AXELOR_ACTION_REFERENCE = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                return arrayOf(AxelorActionReference(element))
            }
        }

        val AXELOR_SELECTION_REFERENCE = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                return arrayOf(AxelorSelectionReference(element))
            }
        }

        val AXELOR_PANEL_DASHLET = object : PsiReferenceProvider() {
            override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

                if(element !is XmlAttributeValueImpl)
                    return PsiReference.EMPTY_ARRAY

                val viewRef = AxelorViewNameReference(element)
                val actionRef = ActionViewMethodReference(element, TextRange(1 , element.text.length -1))
                val arrayOfRef = arrayListOf<PsiReference>()

                viewRef.resolve()?.let{
                    arrayOfRef.add(viewRef)
                }
                actionRef.resolve()?.let{
                    arrayOfRef.add(actionRef)
                }

                return arrayOfRef.toTypedArray()
            }
        }
    }

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(NAME_VIEW, AXELOR_VIEW)
        registrar.registerReferenceProvider(MODEL_DOMAIN, AXELOR_DOMAIN)
        registrar.registerReferenceProvider(ENUM_REF, AXELOR_ENUM)
        registrar.registerReferenceProvider(PlatformPatterns.or(FIELD_NAME, PANEL_RELATED_VIEW), AXELOR_FIELD)
        registrar.registerReferenceProvider(FIELD_NAME_IN_EDITOR, AXELOR_FIELD_IN_EDITOR)
        registrar.registerReferenceProvider(SELECTION, AXELOR_SELECTION)
        registrar.registerReferenceProvider(ACTION_METHOD_VIEW, AXELOR_JAVA_METHOD)
        registrar.registerReferenceProvider(ACTION_VIEW, AXELOR_ACTION)
        registrar.registerReferenceProvider(PlatformPatterns.or(PARENT_MENUITEM, VIEW_REFERENCE), AXELOR_VIEW_REFERENCE)
        registrar.registerReferenceProvider(ACTIONS_NAME, AXELOR_ACTION_REFERENCE)
        registrar.registerReferenceProvider(SELECTION_NAME, AXELOR_SELECTION_REFERENCE)
        registrar.registerReferenceProvider(PANEL_DASHLET_ACTION, AXELOR_PANEL_DASHLET)
    }
}