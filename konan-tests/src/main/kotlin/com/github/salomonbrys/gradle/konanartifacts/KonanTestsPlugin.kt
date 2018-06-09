package com.github.salomonbrys.gradle.konanartifacts

import org.gradle.api.Plugin
import org.gradle.api.Project

class KonanTestsPlugin : Plugin<Project> {

    private fun Project.applyPlugin() {
        extensions.add("konanTests", KonanTestsExtension(project))
    }

    override fun apply(project: Project) = project.applyPlugin()

}
