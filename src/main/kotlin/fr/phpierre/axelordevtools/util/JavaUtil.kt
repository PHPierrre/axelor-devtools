package fr.phpierre.axelordevtools.util

import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiMethod
import com.intellij.psi.search.ProjectScope

class JavaUtil {

    companion object {
        fun findMethod(project: Project, methodName: String, className: String, packageName: String): Array<PsiMethod>? {
            return findMethod(project, methodName, "$packageName.$className")
        }

        fun findMethod(project: Project, methodName: String, packageNameWithClassName: String): Array<PsiMethod>? {
            val aClass = JavaPsiFacade.getInstance(project).findClass(packageNameWithClassName, ProjectScope.getProjectScope(project))
            val methods: Array<PsiMethod>? = aClass?.findMethodsByName(methodName, true)
            return methods
        }
    }
}