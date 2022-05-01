package fr.phpierre.axelordevtools.window

import com.intellij.icons.AllIcons
import javax.swing.JButton
import javax.swing.JPanel

class AxelorGeneralWindow {

    val panel: JPanel = JPanel()
    val refreshEditedViews: JButton = JButton()
    val refreshViews: JButton = JButton()
    val deleteViewsAndRefreshViews: JButton = JButton()
    val deleteMenusAndRefreshViews: JButton = JButton()

    init {
        refreshEditedViews.icon = AllIcons.Actions.BuildLoadChanges
        refreshEditedViews.addActionListener {

        }

        refreshViews.icon = AllIcons.Actions.Refresh
        refreshViews.addActionListener {

        }

        deleteViewsAndRefreshViews.icon = AllIcons.Actions.ForceRefresh
        deleteViewsAndRefreshViews.addActionListener {

        }

        deleteMenusAndRefreshViews.icon = AllIcons.Actions.ListChanges
        deleteMenusAndRefreshViews.addActionListener {

        }

        panel.add(refreshEditedViews)
        panel.add(refreshViews)
        panel.add(deleteViewsAndRefreshViews)
        panel.add(deleteMenusAndRefreshViews)
    }
}