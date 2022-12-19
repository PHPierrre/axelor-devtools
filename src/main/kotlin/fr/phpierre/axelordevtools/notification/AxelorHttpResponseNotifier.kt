package fr.phpierre.axelordevtools.notification

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.Nullable


class AxelorHttpResponseNotifier {

    companion object {
        fun notify(@Nullable project: Project?, content: String, notificationType: NotificationType) {
            NotificationGroupManager.getInstance()
                    .getNotificationGroup("Restore All Notification Group")
                    .createNotification(content, notificationType)
                    .notify(project)
        }
    }
}