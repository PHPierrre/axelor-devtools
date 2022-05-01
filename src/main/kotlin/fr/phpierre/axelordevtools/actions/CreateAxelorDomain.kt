package fr.phpierre.axelordevtools.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import fr.phpierre.axelordevtools.icons.AxelorIcons
import org.jetbrains.annotations.NotNull


class CreateAxelorDomain :
    CreateFileFromTemplateAction("Axelor Domain", "Create Axelor domain", AxelorIcons.Logo), DumbAware {

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("Domain Name").addKind("File", AxelorIcons.Logo, "domain.xml")
    }

    override fun getActionName(directory: PsiDirectory, @NotNull newName: String, templateName: String): String {
        return "Create Domain: $newName"
    }


}