package fr.phpierre.axelordevtools.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlAttribute
import fr.phpierre.axelordevtools.MyBundle

class XmlViewNameInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : XmlElementVisitor() {
            override fun visitXmlAttribute(attribute: XmlAttribute?) {
                super.visitXmlAttribute(attribute)
                if(attribute?.name == "name") {
                    attribute.value?.let {
                        if (it.contains(" ")) {
                            holder.registerProblem(attribute, MyBundle.message("inspection.view.name.space"))
                        }
                    }
                }
            }
        }
    }
}