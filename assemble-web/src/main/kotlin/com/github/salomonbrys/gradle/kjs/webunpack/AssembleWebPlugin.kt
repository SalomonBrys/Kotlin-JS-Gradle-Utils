package com.github.salomonbrys.gradle.kjs.webunpack

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Sync
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.task
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

class AssembleWebPlugin : Plugin<Project> {

    private fun Project.applyPlugin() {
        afterEvaluate {
            val assembleWeb = task<Sync>("assembleWeb") {
                group = "build"
                (configurations["compileClasspath"] + configurations["runtimeClasspath"]).forEach { file ->
                    from(zipTree(file.absolutePath)) {
                        includeEmptyDirs = false
                        include { fileTreeElement ->
                            val path = fileTreeElement.path
                            !path.endsWith(".kjsm") && !path.endsWith(".class") && (path.startsWith("META-INF/resources/") || !path.startsWith("META-INF/"))
                        }

                    }
                }
                from((tasks["compileKotlin2Js"] as Kotlin2JsCompile).destinationDir)
                into("$projectDir/web/out")
                dependsOn("classes")
            }
            tasks["assemble"].dependsOn(assembleWeb)
        }
    }

    override fun apply(project: Project) = project.applyPlugin()
}
