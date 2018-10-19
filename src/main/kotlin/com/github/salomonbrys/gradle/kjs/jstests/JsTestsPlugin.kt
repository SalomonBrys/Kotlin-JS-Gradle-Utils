package com.github.salomonbrys.gradle.kjs.jstests

import com.moowork.gradle.node.NodeExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.IvyArtifactRepository

class JsTestsPlugin : Plugin<Project> {

    @Suppress("UnstableApiUsage")
    private fun Project.applyPlugin() {
        apply {
            plugin("com.moowork.node")
        }

        val extension = KotlinJsTests(this)
        extensions.add("kotlinJsTests", extension)

        extensions.configure<NodeExtension>("node") {
            version = "10.12.0"
            download = true
        }

        repositories.whenObjectAdded {
            (this as? IvyArtifactRepository)?.metadataSources { artifact() }
        }

    }

    override fun apply(project: Project) = project.applyPlugin()
}
