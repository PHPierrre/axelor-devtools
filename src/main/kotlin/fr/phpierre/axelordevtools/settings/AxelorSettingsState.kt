package fr.phpierre.axelordevtools.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import org.jetbrains.annotations.Nullable

@State(
        name = "fr.phpierre.axelordevtools.settings.AxelorSettingsState",
        storages = [Storage("AxelorIntelliJPlugin.xml")]
)
class AxelorSettingsState : PersistentStateComponent<AxelorSettingsState> {

    var erpUsername: String = "admin"
    var erpPassword: String = "admin"
    var aopVersion: Double = 5.1

    companion object {
        fun getInstance(): AxelorSettingsState {
            return ApplicationManager.getApplication().getService(AxelorSettingsState::class.java)
        }
    }

    @Nullable
    override fun getState(): AxelorSettingsState? {
        return this
    }

    override fun loadState(state: AxelorSettingsState) {
        return XmlSerializerUtil.copyBean(state, this);
    }
}