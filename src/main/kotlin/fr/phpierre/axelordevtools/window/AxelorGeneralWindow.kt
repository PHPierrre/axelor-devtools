package fr.phpierre.axelordevtools.window

import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.icons.AllIcons
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBList
import fr.phpierre.axelordevtools.MyBundle
import fr.phpierre.axelordevtools.http.AxelorHttpClient
import fr.phpierre.axelordevtools.http.AxelorRequestBody
import fr.phpierre.axelordevtools.http.AxelorResponseBody
import fr.phpierre.axelordevtools.http.model.NotifyModel
import fr.phpierre.axelordevtools.http.request.metaview.MetaViewRestoreAll
import fr.phpierre.axelordevtools.http.request.metaview.hotreload.ViewHotReload
import fr.phpierre.axelordevtools.http.scenario.DeleteMenusAndReloadViews
import fr.phpierre.axelordevtools.http.scenario.DeleteViewsAndReloadViews
import fr.phpierre.axelordevtools.notification.AxelorHttpResponseNotifier
import fr.phpierre.axelordevtools.service.EditedViewService
import fr.phpierre.axelordevtools.util.AxelorFile
import icons.GradleIcons
import org.jetbrains.plugins.gradle.action.GradleExecuteTaskAction
import javax.swing.*


class AxelorGeneralWindow(val project: Project) {

    val panel: JPanel = JPanel()
    val vertical: BoxLayout = BoxLayout(panel, BoxLayout.Y_AXIS)
    val refreshEditedViews: JButton = JButton()
    val refreshViews: JButton = JButton()
    val deleteViewsAndRefreshViews: JButton = JButton()
    val deleteMenusAndRefreshViews: JButton = JButton()
    var test: JList<String> = JList()

    init {
        panel.layout = vertical
        refreshEditedViews.icon = AllIcons.Actions.BuildLoadChanges
        refreshEditedViews.text = MyBundle.message("window.btn.reload.edited.views")
        refreshEditedViews.addActionListener {
            AxelorHttpResponseNotifier.notify(project, MyBundle.message("notification.reloading.edited.views"), NotificationType.INFORMATION)

            ApplicationManager.getApplication().executeOnPooledThread {
                ApplicationManager.getApplication().runReadAction {

                    val files : Set<String> = AxelorFile.toLocalePath(EditedViewService.getViews())
                    val responseBody: AxelorResponseBody<List<NotifyModel>> = AxelorHttpClient.request(ViewHotReload(files))

                    invokeLater {
                        responseBody.data?.let {
                            AxelorHttpResponseNotifier.notify(project, it[0].notify, NotificationType.INFORMATION)
                        }
                        EditedViewService.reset();
                    }
                }
            }
        }

        refreshViews.icon = AllIcons.Actions.Refresh
        refreshViews.text = MyBundle.message("window.btn.reload.all.views")
        refreshViews.addActionListener {
            AxelorHttpResponseNotifier.notify(project, MyBundle.message("notification.reloading.views"), NotificationType.INFORMATION)

            ApplicationManager.getApplication().executeOnPooledThread {
                ApplicationManager.getApplication().runReadAction {
                    val responseBody: AxelorResponseBody<List<NotifyModel>> = AxelorHttpClient.request(MetaViewRestoreAll())
                    invokeLater {
                        responseBody.data?.let {
                            AxelorHttpResponseNotifier.notify(project, it[0].notify, NotificationType.INFORMATION)
                        }
                    }
                }
            }
        }

        deleteViewsAndRefreshViews.icon = AllIcons.Actions.ForceRefresh
        deleteViewsAndRefreshViews.text = MyBundle.message("window.btn.delete.views")
        deleteViewsAndRefreshViews.toolTipText = MyBundle.message("window.btn.delete.views")
        deleteViewsAndRefreshViews.addActionListener {
            AxelorHttpResponseNotifier.notify(project, MyBundle.message("notification.delete.and.reloading.views"), NotificationType.INFORMATION)

            //Je m'appelle Pierre, je suis marié à une superbe femme nommée Clara. C'est l'amour de ma vie donc je prends soin d'elle.
            //Cette femme me rend très heureux et je la rends très heureuse.
            // Pour ça, il faut savoir bien communiquer, être attentif, ne pas se reposer sur ses acquis et faire vibrer les sentiments.
            // C'est donc un travail de tous les jours. Ce n'est pas facile, car cela demande une attention constante, mais c'est ainsi que fonctionne le vrai amour.
            // L'amour, c'est aussi une course d'endurance, il ne faut pas utiliser toutes ses ressources au début.

            ApplicationManager.getApplication().executeOnPooledThread {
                ApplicationManager.getApplication().runReadAction {
                    val responseBody: AxelorResponseBody<List<NotifyModel>> = DeleteViewsAndReloadViews(project).execute()
                    invokeLater {
                        responseBody.data?.let {
                            AxelorHttpResponseNotifier.notify(project, it[0].notify, NotificationType.INFORMATION)
                        }
                    }
                }
            }
        }

        deleteMenusAndRefreshViews.icon = AllIcons.Actions.ListChanges
        deleteMenusAndRefreshViews.text = MyBundle.message("window.btn.reload.menus")
        deleteMenusAndRefreshViews.toolTipText = MyBundle.message("window.btn.delete.menus.and.views")
        deleteMenusAndRefreshViews.addActionListener {
            AxelorHttpResponseNotifier.notify(project, MyBundle.message("notification.delete.menus.and.reloading.views"), NotificationType.INFORMATION)

            ApplicationManager.getApplication().executeOnPooledThread {
                ApplicationManager.getApplication().runReadAction {
                    val responseBody: AxelorResponseBody<List<NotifyModel>> = DeleteMenusAndReloadViews(project).execute()
                    invokeLater {
                        responseBody.data?.let {
                            AxelorHttpResponseNotifier.notify(project, it[0].notify, NotificationType.INFORMATION)
                        }
                    }
                }
            }
        }

        panel.add(refreshEditedViews)
        panel.add(refreshViews)
        panel.add(deleteViewsAndRefreshViews)
        panel.add(deleteMenusAndRefreshViews)

        // Gradle buttons

        val gradleBuild: JButton = JButton()
        gradleBuild.icon = GradleIcons.Gradle
        gradleBuild.text = "build"
        gradleBuild.addActionListener {
            project.basePath?.let {
                GradleExecuteTaskAction.runGradle(
                        project,
                        ApplicationManager.getApplication().getService(DefaultRunExecutor::class.java),
                        it,
                        "build")
            }
        }
        panel.add(gradleBuild)

        val gradleGenerateCode: JButton = JButton()
        gradleGenerateCode.icon = GradleIcons.Gradle
        gradleGenerateCode.text = "generateCode"
        gradleGenerateCode.addActionListener {
            project.basePath?.let {
                GradleExecuteTaskAction.runGradle(
                        project,
                        ApplicationManager.getApplication().getService(DefaultRunExecutor::class.java),
                        it,
                        "generateCode")
            }
        }
        panel.add(gradleGenerateCode)

        panel.add(JLabel("Edited views"))

        test = JBList(EditedViewService.getListModel()) // A tester

        panel.add(JScrollPane(test))
    }
}