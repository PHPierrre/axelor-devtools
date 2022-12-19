package fr.phpierre.axelordevtools.actions

import com.intellij.ide.fileTemplates.DefaultTemplatePropertiesProvider
import com.intellij.psi.PsiDirectory
import fr.phpierre.axelordevtools.settings.AxelorSettingsState
import java.util.*

class AxelorDefaultTemplatePropertiesProvider : DefaultTemplatePropertiesProvider {

    val ATTRIBUTE_AXELOR_VERSION: String = "AXELOR_VERSION"

    override fun fillProperties(directory: PsiDirectory, props: Properties) {
        val settings: AxelorSettingsState = AxelorSettingsState.getInstance()
        props.setProperty(ATTRIBUTE_AXELOR_VERSION, settings.aopVersion.toString())
    }
}