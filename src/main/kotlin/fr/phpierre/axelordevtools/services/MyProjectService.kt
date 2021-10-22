package fr.phpierre.axelordevtools.services

import com.intellij.openapi.project.Project
import fr.phpierre.axelordevtools.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
