package fr.phpierre.axelordevtools.listener

import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import fr.phpierre.axelordevtools.service.EditedViewService
import fr.phpierre.axelordevtools.util.AxelorFile

class EditedViewListener : BulkFileListener {

    override fun after(events: MutableList<out VFileEvent>) {
        for (e in events) {
            if(e.isFromSave && AxelorFile.isView(e.file!!)) {
                EditedViewService.addView(e.file!!)
            }
        }
    }
}