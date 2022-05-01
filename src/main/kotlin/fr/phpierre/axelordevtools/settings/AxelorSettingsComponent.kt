package fr.phpierre.axelordevtools.settings

import com.intellij.ui.CollectionListModel
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JPanel

class AxelorSettingsComponent {

    var panel: JPanel
    val erpUsername: JBTextField = JBTextField()
    val erpPassword: JBTextField = JBTextField()
    var aopVersion: JBList<Double> = JBList()

    init {

        val fieldsCollection: CollectionListModel<Double> = CollectionListModel()
        fieldsCollection.add(5.1)
        fieldsCollection.add(5.2)
        fieldsCollection.add(5.3)
        fieldsCollection.add(5.4)
        aopVersion = JBList(fieldsCollection)

        panel = FormBuilder.createFormBuilder()
                .addLabeledComponent(JBLabel("Axelor ERP username"), erpUsername)
                .addLabeledComponent(JBLabel("Axelor ERP password"), erpPassword)
                .addLabeledComponent(JBLabel("Axelor AOP version"), aopVersion)
                .panel
    }

}