package fr.phpierre.axelordevtools.window

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.content.ContentManager

class AxelorToolWindow : ToolWindowFactory{

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentManager: ContentManager = toolWindow.contentManager
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(AxelorGeneralWindow().panel, "", false)
        contentManager.addContent(content)
    }

}