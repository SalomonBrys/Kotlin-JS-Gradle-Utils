package com.github.salomonbrys.gradle.kjs.assembleweb

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Sync
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.task
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

class AssembleWebPlugin : Plugin<Project> {

    private val forbiddenExtensions = listOf(".kjsm", ".class", ".metadata", ".kotlin_metadata")

    private fun Project.applyPlugin() {
        val ext = AssembleWebExtension(this)
        extensions.add("assembleWeb", ext)

        afterEvaluate {
            val assembleWebJs = task<Sync>("assembleWebJs") {
                group = "build"
                (configurations["compileClasspath"] + configurations["runtimeClasspath"]).forEach { file ->
                    from(zipTree(file.absolutePath)) {
                        includeEmptyDirs = false
                        include { fileTreeElement ->
                            val path = fileTreeElement.path
                            (
                                    forbiddenExtensions.none { path.endsWith(it) }
                                &&  (
                                        path.startsWith("META-INF/resources/")
                                    ||  !path.startsWith("META-INF/")
                                    )
                                )
                        }
                        eachFile {
                            if (path.startsWith("META-INF/resources/"))
                                path = path.substring(19)
                        }

                    }
                }
                from((tasks["compileKotlin2Js"] as Kotlin2JsCompile).destinationDir)
                into(ext.outputDir)
                dependsOn("classes")
            }
            tasks["assemble"].dependsOn(assembleWebJs)
        }
    }

    override fun apply(project: Project) = project.applyPlugin()
}
