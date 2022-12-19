package fr.phpierre.axelordevtools.http.scenario

import com.intellij.notification.NotificationType
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.project.Project
import fr.phpierre.axelordevtools.MyBundle
import fr.phpierre.axelordevtools.http.AxelorHttpClient
import fr.phpierre.axelordevtools.http.AxelorResponseBody
import fr.phpierre.axelordevtools.http.model.AxelorModel
import fr.phpierre.axelordevtools.http.model.NotifyModel
import fr.phpierre.axelordevtools.http.request.metaview.MetaViewRemoveAll
import fr.phpierre.axelordevtools.http.request.metaview.MetaViewRestoreAll
import fr.phpierre.axelordevtools.http.request.metaview.MetaViewSearch
import fr.phpierre.axelordevtools.notification.AxelorHttpResponseNotifier

class DeleteViewsAndReloadViews(val project: Project) : HttpScenario<List<NotifyModel>> {


    override fun execute(): AxelorResponseBody<List<NotifyModel>> {
        val responseBody: AxelorResponseBody<List<AxelorModel>> = AxelorHttpClient.request(MetaViewSearch())
        val views: List<AxelorModel>? = responseBody.data
        views?.let {
            AxelorHttpResponseNotifier.notify(project,
                    MyBundle.message("delete") + " " + views.size + " " + MyBundle.message("views"),
                    NotificationType.INFORMATION)
            val t = AxelorHttpClient.request(MetaViewRemoveAll(views))
        }
        
        return AxelorHttpClient.request(MetaViewRestoreAll())
    }
}