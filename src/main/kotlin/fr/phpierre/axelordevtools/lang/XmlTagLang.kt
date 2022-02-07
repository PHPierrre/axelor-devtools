package fr.phpierre.axelordevtools.lang

import fr.phpierre.axelordevtools.objects.xml.action.*
import fr.phpierre.axelordevtools.objects.xml.view.MetaFormView
import fr.phpierre.axelordevtools.objects.xml.view.MetaGridView
import fr.phpierre.axelordevtools.objects.xml.widget.Button
import fr.phpierre.axelordevtools.objects.xml.widget.Field
import fr.phpierre.axelordevtools.objects.xml.widget.PanelInclude

class XmlTagLang {
    companion object {

        const val axelorViewGridType = "grid"
        const val axelorViewFormType = "form"
        const val axelorViewTreeType = "tree"
        const val axelorViewChartType = "chart"
        const val axelorViewCalendarType = "calendar"
        const val axelorViewKanbanType = "kanban"
        const val axelorViewCardsType = "cards"
        const val axelorViewCustomType = "custom"
        const val axelorViewGanttType = "gantt"
        const val axelorViewDashboardType = "dashboard"

        val viewType = setOf(
            axelorViewGridType,
            axelorViewFormType,
            axelorViewTreeType,
            axelorViewChartType,
            axelorViewCalendarType,
            axelorViewKanbanType,
            axelorViewCardsType,
            axelorViewCustomType,
            axelorViewGanttType,
            axelorViewDashboardType)

        val actionType = setOf(
            "action-view",
            "action-attrs",
            "action-record",
            "action-method",
            "action-script",
            "action-validate",
            "action-condition",
            "action-group",
            "action-import",
            "action-export",
            "action-ws"
        )

        val actionEvents = setOf("onNew", "onLoad", "onLoad", "onSave", "onDelete", "onChange", "onSelect", "onClick", "onTabSelect")

        // Xml tag which can target view
        val viewReferences = mapOf(
            "field" to Field::class.java,
            "panel-include" to PanelInclude::class.java,
            "action-view" to ActionView::class.java,
        )

        // Xml tag which can target action
        val actionReferences = mapOf(
            "grid" to MetaGridView::class.java,
            "form" to MetaFormView::class.java,
            "button" to Button::class.java

//            "action-attrs" to ActionAttrs::class.java,
//            "action-method" to ActionMethod::class.java,
//            "action-record" to ActionRecord::class.java,
//            "action-validate" to ActionValidate::class.java,
        )
    }
}