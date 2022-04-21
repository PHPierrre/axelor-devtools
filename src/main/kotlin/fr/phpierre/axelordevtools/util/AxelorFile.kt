package fr.phpierre.axelordevtools.util

import com.intellij.psi.PsiElement

class AxelorFile {

    companion object {

        private const val RESOURCES_FOLDER = "resources"

        private const val VIEWS_FOLDER = "views"
        private const val DOMAINS_FOLDER = "domains"

        private fun getFolderBeforeResourceFolder(element: PsiElement): String {
            var currentElement = element.containingFile.containingDirectory
            var lastDirectory = element.containingFile.containingDirectory.name

            while(currentElement.name != RESOURCES_FOLDER) {
                currentElement = currentElement.parent
                lastDirectory = currentElement.name
            }

            return lastDirectory
        }

        fun isDomain(element: PsiElement): Boolean {
            return getFolderBeforeResourceFolder(element) == DOMAINS_FOLDER
        }

        fun isView(element: PsiElement): Boolean {
            return getFolderBeforeResourceFolder(element) == VIEWS_FOLDER
        }
    }
}