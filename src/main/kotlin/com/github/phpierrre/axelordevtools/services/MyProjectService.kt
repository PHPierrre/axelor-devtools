package com.github.phpierrre.axelordevtools.services

import com.intellij.openapi.project.Project
import com.github.phpierrre.axelordevtools.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
