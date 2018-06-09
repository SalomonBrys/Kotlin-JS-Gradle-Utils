package com.github.salomonbrys.gradle.kjs.jstests

import de.solugo.gradle.nodejs.NodeJsExtension
import de.solugo.gradle.nodejs.NodeJsTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.artifacts.dependencies.DefaultProjectDependency
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getPluginByName
import org.gradle.kotlin.dsl.task
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

class JsTestsPlugin : Plugin<Project> {

    private val Project.sourceSets: SourceSetContainer
        get() {
            val java = convention.getPluginByName<org.gradle.api.plugins.JavaPluginConvention>("java")
            return java.sourceSets
        }

    private fun Project.applyPlugin() {
        apply {
            plugin("de.solugo.gradle.nodejs")
        }

        val extension = KotlinJsTestsExtension()
        extensions.add("jsTests", extension)

        val compileKotlin2Js = tasks["compileKotlin2Js"] as Kotlin2JsCompile
        val compileTestKotlin2Js = tasks["compileTestKotlin2Js"] as Kotlin2JsCompile

        compileTestKotlin2Js.kotlinOptions.moduleKind = "commonjs"

        val populateNodeModules = task<Copy>("populateNodeModules") {
            dependsOn(compileKotlin2Js)
            into("$buildDir/node_modules")
        }

        afterEvaluate {
            populateNodeModules.apply {
                from(compileKotlin2Js.destinationDir)

                (configurations["testCompileClasspath"] + configurations["testRuntimeClasspath"]).forEach {
                    from(zipTree(it.absolutePath).matching { include("*.js") })
                }
            }
        }

        extensions.configure<NodeJsExtension>("nodejs") {
            version = "10.1.0"
        }

        val runJSTests = task<NodeJsTask<*>>("runJSTests") {
            dependsOn(compileTestKotlin2Js, populateNodeModules)
            setRequire(extension.engine.npmModules)
            setExecutable(extension.engine.executable)
            setArgs(extension.engine.firstArguments + listOf(projectDir.toPath().relativize(file(compileTestKotlin2Js.outputFile).toPath())))
        }

        afterEvaluate {
            if (sourceSets["test"].allSource.isEmpty) {
                val expectedBy = configurations.firstOrNull { it.name == "expectedBy" } ?: run {
                    logger.info("${project.path}: Project has neither test sources nor expectedBy dependency: skipping tests.")
                    return@afterEvaluate
                }

                val hasTestSources = expectedBy.dependencies.any {
                    val commonProject = (it as DefaultProjectDependency).dependencyProject
                    !commonProject.sourceSets["test"].allSource.isEmpty
                }
                if (!hasTestSources) {
                    logger.info("${project.path}: Project and all expectedBy dependencies have no test sources : skipping tests.")
                    return@afterEvaluate
                }
                else
                    logger.info("${project.path}: Project has no test sources but expectedBy dependencies have.")
            }
            else
                logger.info("${project.path}: Project has test sources")

            tasks["test"].dependsOn(runJSTests)
        }
    }

    override fun apply(project: Project) = project.applyPlugin()
}
