package fr.phpierre.axelordevtools.lang

class XmlTagLang {
    companion object {
        val viewType = setOf(
                "grid",
                "form",
                "tree",
                "chart",
                "calendar",
                "kanban",
                "cards",
                "custom",
                "gantt",
                "dashboard")

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

        val viewReferences = setOf("from-view", "grid-view", "view")
    }
}