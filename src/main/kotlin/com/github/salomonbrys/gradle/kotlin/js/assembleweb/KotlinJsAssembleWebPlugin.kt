package com.github.salomonbrys.gradle.kotlin.js.assembleweb

import com.github.salomonbrys.gradle.kotlin.js.KtPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Sync
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.task
import kotlin.reflect.KClass

abstract class KotlinJsAssembleWebPlugin<E : KotlinJsAssembleWebExtension> : KtPlugin<Project> {

    protected abstract val extensionClass: KClass<E>

    protected abstract fun listTargets(ext: E): Iterable<KotlinJsWebTarget>

    final override fun Project.applyPlugin() {
        val ext = project.extensions.create("assembleWeb", extensionClass.java, this)

        afterEvaluate {
            listTargets(ext).forEach { target ->
                addAssembleWebTasks(this, target)
            }

        }
    }

    private val forbiddenExtensions = listOf(".kjsm", ".class", ".metadata", ".kotlin_metadata")

    private fun addAssembleWebTasks(project: Project, target: KotlinJsWebTarget) {

        val assembleWeb = project.task<Sync>("${target.name}AssembleWeb") {
            group = "build"
            dependsOn(target.compileTask)

            from(target.compileTask.destinationDir)
            into(target.outputDir)
        }

        val prepareAssembleWeb = project.task<DefaultTask>("${target.name}PrepareAssembleWeb") {
            doLast {
                assembleWeb.apply {
                    target.configurationNames
                            .flatMap { project.configurations[it] }
                            .forEach { file ->
                                from(project.zipTree(file.absolutePath)) {
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
                }
            }
        }

        assembleWeb.dependsOn(prepareAssembleWeb)

        project.tasks["assemble"].dependsOn(assembleWeb)

    }

}
