package fr.phpierre.axelordevtools.settings

import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.javaee.appServers.run.configuration.JavaeeRunConfigurationCommonSettingsBean
import com.intellij.javaee.appServers.run.configuration.view.JavaeeRunConfigurationEditorContext
import com.intellij.javaee.appServers.serverInstances.ApplicationServersManager
import com.intellij.javaee.web.deployment.JspDeploymentManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.ui.CollectionListModel
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import org.jetbrains.idea.tomcat.server.TomcatConfiguration
import org.jetbrains.idea.tomcat.server.TomcatIntegration
import org.jetbrains.idea.tomcat.server.TomcatLocalModel
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JList
import javax.swing.JPanel

class AxelorSettingsComponent {

    var panel: JPanel
    var erpUrl: JBTextField = JBTextField()
    val erpUsername: JBTextField = JBTextField()
    val erpPassword: JBTextField = JBTextField()
    var aopVersion: JList<Double> = JBList()
    var hotReloadActivated: JCheckBox = JCheckBox()
    //val useTomcatHost: JButton = JButton("Use Tomcat config")

    init {

        val fieldsCollection: CollectionListModel<Double> = CollectionListModel()
        fieldsCollection.add(5.1)
        fieldsCollection.add(5.2)
        fieldsCollection.add(5.3)
        fieldsCollection.add(5.4)
        aopVersion = JBList(fieldsCollection)

        /*useTomcatHost.addActionListener {
            val t = ApplicationServersManager.getInstance().applicationServers.get(0)
            val u = TomcatConfiguration.getInstance()
            val v = TomcatIntegration.getInstance()
            print("ok")
        }*/

        panel = FormBuilder.createFormBuilder()
                .addLabeledComponent(JBLabel("Axelor ERP url"), erpUrl)
                //.addComponent(useTomcatHost)
                .addLabeledComponent(JBLabel("Axelor ERP username"), erpUsername)
                .addLabeledComponent(JBLabel("Axelor ERP password"), erpPassword)
                .addLabeledComponent(JBLabel("Axelor AOP version"), aopVersion)
                .addLabeledComponent(JBLabel("Activate Axelor views hot reload"), hotReloadActivated)
                .panel
    }

}