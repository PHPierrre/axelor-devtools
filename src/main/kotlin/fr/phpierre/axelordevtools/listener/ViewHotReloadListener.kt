package fr.phpierre.axelordevtools.listener

import com.intellij.compiler.impl.ExitStatus
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.compiler.CompilationStatusListener
import com.intellij.openapi.compiler.CompileContext
import com.intellij.openapi.compiler.CompilerMessageCategory
import com.intellij.openapi.util.Key
import com.intellij.xdebugger.XDebuggerManager
import fr.phpierre.axelordevtools.http.AxelorHttpClient
import fr.phpierre.axelordevtools.http.AxelorResponseBody
import fr.phpierre.axelordevtools.http.model.NotifyModel
import fr.phpierre.axelordevtools.http.request.metaview.hotreload.ViewHotReload
import fr.phpierre.axelordevtools.notification.AxelorHttpResponseNotifier
import fr.phpierre.axelordevtools.service.EditedViewService
import fr.phpierre.axelordevtools.settings.AxelorSettingsState
import fr.phpierre.axelordevtools.util.AxelorFile

class ViewHotReloadListener : CompilationStatusListener {

    private val COMPILE_SERVER_BUILD_STATUS = Key.create<ExitStatus>("COMPILE_SERVER_BUILD_STATUS")

    override fun compilationFinished(aborted: Boolean, errors: Int, warnings: Int, compileContext: CompileContext) {
        val settings: AxelorSettingsState = AxelorSettingsState.getInstance()

        // If Axelor hot reload is activated
        if(!settings.hotReloadActivated) {
            return
        }

        if(DebuggerListener.isPause) {
            print("pause")
            return
        }
        print("on")

        if(aborted || errors > 0) return

        ApplicationManager.getApplication().executeOnPooledThread {

            // If axelor is not started or offline
            if(!AxelorHttpClient.isOnline()) {
                return@executeOnPooledThread
            }

            if(compileContext.getMessageCount(CompilerMessageCategory.INFORMATION) > 0) {
                val files = AxelorFile.toLocalePath(EditedViewService.getViews())
                if(files.isEmpty()) {
                    return@executeOnPooledThread
                }

                val responseBody: AxelorResponseBody<List<NotifyModel>> = AxelorHttpClient.request(ViewHotReload(files))
                responseBody.data?.let {
                    AxelorHttpResponseNotifier.notify(compileContext.project, it[0].notify, NotificationType.INFORMATION)
                    EditedViewService.reset();
                }
            }

        }
    }

}