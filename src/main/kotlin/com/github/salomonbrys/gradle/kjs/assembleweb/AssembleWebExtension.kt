package com.github.salomonbrys.gradle.kjs.assembleweb

import org.gradle.api.Project

class AssembleWebExtension(project: Project) {
    var outputDir = "${project.buildDir}/web/kjs"
}
