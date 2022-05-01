package fr.phpierre.axelordevtools.notification

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotifications
import fr.phpierre.axelordevtools.MyBundle
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import org.jetbrains.plugins.gradle.service.settings.GradleConfigurable
import org.jetbrains.plugins.gradle.settings.GradleProjectSettings
import org.jetbrains.plugins.gradle.settings.TestRunner


class SetupGradleBuildTool(project: Project) : EditorNotifications.Provider<EditorNotificationPanel>(), DumbAware {
    private val KEY = Key.create<EditorNotificationPanel>("axelor.gradle.build.tool")

    @Nullable
    override fun createNotificationPanel(@NotNull virtualFile: VirtualFile, @NotNull fileEditor: FileEditor, @NotNull project: Project): EditorNotificationPanel? {
        val delegateBuild = GradleProjectSettings.isDelegatedBuildEnabled(project, project.basePath)
        val testRunner = GradleProjectSettings.getTestRunner(project, project.basePath)
        if(!delegateBuild && testRunner == TestRunner.PLATFORM) {
            return null
        }

        val panel = EditorNotificationPanel()
        panel.text = MyBundle.message("gradle.build.tool.need.to.be.configured")
        panel.createActionLabel(MyBundle.message("gradle.build.tool.configure")) {
            ApplicationManager.getApplication().invokeLater {
                ShowSettingsUtil.getInstance().showSettingsDialog(project, GradleConfigurable::class.java)
            }
        }

        return panel
    }

    override fun getKey(): Key<EditorNotificationPanel> {
        return KEY
    }
}
