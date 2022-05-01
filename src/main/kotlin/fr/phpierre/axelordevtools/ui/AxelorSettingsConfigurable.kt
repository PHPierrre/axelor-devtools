package fr.phpierre.axelordevtools.ui

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import fr.phpierre.axelordevtools.settings.AxelorSettingsComponent
import fr.phpierre.axelordevtools.settings.AxelorSettingsState
import org.jetbrains.annotations.NotNull
import javax.swing.JComponent

class AxelorSettingsConfigurable(@NotNull project: Project) : Configurable{

    private lateinit var axelorSettingsComponent: AxelorSettingsComponent

    override fun createComponent(): JComponent? {
        axelorSettingsComponent = AxelorSettingsComponent()
        return axelorSettingsComponent.panel
    }

    override fun isModified(): Boolean {
        val settings: AxelorSettingsState = AxelorSettingsState.getInstance()

        return settings.erpUsername != axelorSettingsComponent.erpUsername.text
                || settings.erpPassword != axelorSettingsComponent.erpPassword.text
                || settings.aopVersion != axelorSettingsComponent.aopVersion.selectedValue
    }

    override fun apply() {
        val settings: AxelorSettingsState = AxelorSettingsState.getInstance()
        settings.erpUsername = axelorSettingsComponent.erpUsername.text
        settings.erpPassword = axelorSettingsComponent.erpPassword.text
        settings.aopVersion = axelorSettingsComponent.aopVersion.selectedValue
    }

    override fun reset() {
        val settings: AxelorSettingsState = AxelorSettingsState.getInstance()
        axelorSettingsComponent.erpUsername.text = settings.erpUsername
        axelorSettingsComponent.erpPassword.text = settings.erpPassword
        axelorSettingsComponent.aopVersion.setSelectedValue(settings.aopVersion, true)
    }

    override fun getDisplayName(): String {
        return "Axelor Plugin"
    }
}