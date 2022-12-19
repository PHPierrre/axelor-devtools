package fr.phpierre.axelordevtools.http.scenario

import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import fr.phpierre.axelordevtools.MyBundle
import fr.phpierre.axelordevtools.http.AxelorHttpClient
import fr.phpierre.axelordevtools.http.AxelorResponseBody
import fr.phpierre.axelordevtools.http.model.AxelorModel
import fr.phpierre.axelordevtools.http.model.NotifyModel
import fr.phpierre.axelordevtools.http.request.metamenu.MetaMenuRemoveAll
import fr.phpierre.axelordevtools.http.request.metamenu.MetaMenuSearch
import fr.phpierre.axelordevtools.http.request.metaview.MetaViewRestoreAll
import fr.phpierre.axelordevtools.notification.AxelorHttpResponseNotifier

class DeleteMenusAndReloadViews(val project: Project) : HttpScenario<List<NotifyModel>> {

    override fun execute(): AxelorResponseBody<List<NotifyModel>> {
        val responseBody: AxelorResponseBody<List<AxelorModel>> = AxelorHttpClient.request(MetaMenuSearch())
        val menus: List<AxelorModel>? = responseBody.data

        menus?.let {
            AxelorHttpResponseNotifier.notify(project,
                    MyBundle.message("delete") + " " + menus.size + " " + MyBundle.message("menus"),
                    NotificationType.INFORMATION
            )
            val t = AxelorHttpClient.request(MetaMenuRemoveAll(menus))
        }

        return AxelorHttpClient.request(MetaViewRestoreAll())
    }

}