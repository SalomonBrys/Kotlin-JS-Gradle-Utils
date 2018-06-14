package com.github.salomonbrys.gradle.kjs.jstests

import com.moowork.gradle.node.NodeExtension
import com.moowork.gradle.node.npm.NpmTask
import com.moowork.gradle.node.task.NodeTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.ArtifactRepository
import org.gradle.api.artifacts.repositories.IvyArtifactRepository
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
            plugin("com.moowork.node")
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

        extensions.configure<NodeExtension>("node") {
            version = "10.1.0"
            download = true
        }

        repositories.whenObjectAdded {
            (this as? IvyArtifactRepository)?.metadataSources { artifact() }
        }

        afterEvaluate {
            populateNodeModules.apply {
                from(compileKotlin2Js.destinationDir)

                (configurations["testCompileClasspath"] + configurations["testRuntimeClasspath"]).forEach {
                    from(zipTree(it.absolutePath).matching { include("*.js") })
                }
            }

            val engineName = extension.engine.npmModules.first().capitalize()

            val installJSTestRunner = task<NpmTask>("install$engineName") {
                group = "verification"
                setArgs(listOf("install") + extension.engine.npmModules)
            }

            val runJSTests = task<NodeTask>("run$engineName") {
                dependsOn(compileTestKotlin2Js, populateNodeModules, installJSTestRunner)
                group = "verification"
                setScript(file(extension.engine.executable))
                setArgs(extension.engine.firstArguments + listOf(projectDir.toPath().relativize(file(compileTestKotlin2Js.outputFile).toPath())))
            }

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
