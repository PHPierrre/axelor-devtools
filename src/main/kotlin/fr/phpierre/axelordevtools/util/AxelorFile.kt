package fr.phpierre.axelordevtools.util

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement

class AxelorFile {

    companion object {

        private const val RESOURCES_FOLDER = "resources"

        private const val VIEWS_FOLDER = "views"
        private const val DOMAINS_FOLDER = "domains"

        private fun getFolderBeforeResourceFolder(element: PsiElement): String {
            var currentElement = element.containingFile.containingDirectory
            var lastDirectory = element.containingFile.containingDirectory.name

            while(currentElement.name != RESOURCES_FOLDER && currentElement.parent != null) {
                lastDirectory = currentElement.name
                currentElement = currentElement.parent
            }

            return lastDirectory
        }

        private fun getFolderBeforeResourceFolder(virtualFile: VirtualFile): String {
            var currentElement = virtualFile.parent
            var lastDirectory = virtualFile.parent.name

            while(currentElement.name != RESOURCES_FOLDER && currentElement.parent != null) {
                lastDirectory = currentElement.name
                currentElement = currentElement.parent
            }

            return lastDirectory
        }

        fun isDomain(element: PsiElement): Boolean {
            return getFolderBeforeResourceFolder(element) == DOMAINS_FOLDER
        }

        fun isView(element: PsiElement): Boolean {
            return getFolderBeforeResourceFolder(element) == VIEWS_FOLDER
        }

        fun isDomain(virtualFile: VirtualFile): Boolean {
            return getFolderBeforeResourceFolder(virtualFile) == DOMAINS_FOLDER
        }

        fun isView(virtualFile: VirtualFile): Boolean {
            return getFolderBeforeResourceFolder(virtualFile) == VIEWS_FOLDER
        }

        fun toLocalePath(views: Set<VirtualFile>): Set<String> {
            val paths: MutableSet<String> = mutableSetOf()
            for(view in views) {
                paths.add(view.path)
            }
            return paths
        }
    }
}