package fr.phpierre.axelordevtools.references.java

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.xml.XmlTag
import fr.phpierre.axelordevtools.util.JavaUtil
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import org.jetbrains.plugins.gradle.settings.GradleLocalSettings
import org.jetbrains.plugins.gradle.settings.GradleProjectSettings
import org.jetbrains.plugins.gradle.settings.GradleSettings
import org.jetbrains.plugins.gradle.settings.GradleSystemSettings
import org.jetbrains.plugins.gradle.util.GradleUtil

class JavaMethodReference(@NotNull element: PsiElement) : PsiReferenceBase<PsiElement?>(element),
        PsiPolyVariantReference {

    @Nullable
    override fun resolve(): PsiElement? {
        val resolveResults: Array<ResolveResult> = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val results: MutableList<ResolveResult> = ArrayList()
        val project: Project = myElement!!.project
        val range = TextRange(1, element.text.length - 1)
        val method = element.text.substring(range.startOffset, range.endOffset)
        val xmlTag: XmlTag = element.parent.parent as XmlTag
        val packageWithClassName = xmlTag.getAttribute("class")

        packageWithClassName?.value?.let {
            val properties: Array<PsiMethod>? = JavaUtil.findMethod(project, method, it)
            properties?.let {
                for (property in properties) {
                    results.add(PsiElementResolveResult(property))
                }
            }
        }

        return results.toTypedArray()
    }
}
