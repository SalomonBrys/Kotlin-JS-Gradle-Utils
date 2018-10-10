package com.github.salomonbrys.gradle.kjs.jstests

import org.gradle.api.Plugin
import org.gradle.api.Project

class JsTestsPlugin : Plugin<Project> {

    private fun Project.applyPlugin() {
        apply {
            plugin("com.moowork.node")
        }

        val extension = KotlinJsTests(this)
        extensions.add("kotlinJsTests", extension)
    }

    override fun apply(project: Project) = project.applyPlugin()
}
