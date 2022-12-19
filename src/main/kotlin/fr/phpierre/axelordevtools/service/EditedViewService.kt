package fr.phpierre.axelordevtools.service

import com.intellij.openapi.vfs.VirtualFile
import fr.phpierre.axelordevtools.util.AxelorFile
import javax.swing.AbstractListModel
import javax.swing.DefaultListModel

object EditedViewService {

    private val views: MutableSet<VirtualFile> = mutableSetOf()
    val model: DefaultListModel<String> = DefaultListModel()

    fun getViews(): Set<VirtualFile> {
        return views
    }

    fun addView(view: VirtualFile) {
        if(!views.contains(view)) {
            model.addElement(view.path)
            views.add(view)
        }
    }

    fun getListModel(): DefaultListModel<String> {
        return model
    }

    fun reset() {
        views.clear();
        model.clear();
    }
}