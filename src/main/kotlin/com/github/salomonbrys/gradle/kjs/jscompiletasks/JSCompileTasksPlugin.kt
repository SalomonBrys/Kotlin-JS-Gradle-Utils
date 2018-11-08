package com.github.salomonbrys.gradle.kjs.jscompiletasks

import org.gradle.api.Plugin
import org.gradle.api.Project

class JSCompileTasksPlugin : Plugin<Project> {

    @Suppress("UnstableApiUsage")
    private fun Project.applyPlugin() {
        extensions.add("JSCompileTasks", JSCompileTasksExtension(this))
    }

    override fun apply(project: Project) = project.applyPlugin()
}
