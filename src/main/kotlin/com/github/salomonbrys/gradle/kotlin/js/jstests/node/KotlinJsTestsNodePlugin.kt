package com.github.salomonbrys.gradle.kotlin.js.jstests.node

import com.github.salomonbrys.gradle.kotlin.js.KtPlugin
import com.moowork.gradle.node.NodeExtension
import com.moowork.gradle.node.npm.NpmTask
import com.moowork.gradle.node.task.NodeTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.repositories.IvyArtifactRepository
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.task
import kotlin.properties.Delegates
import kotlin.reflect.KClass

abstract class KotlinJsTestsNodePlugin<E: KotlinJsTestsNodeExtension> : KtPlugin<Project> {

    protected abstract val extensionClass: KClass<E>

    protected abstract fun listTargets(ext: E): Iterable<KotlinJsTestsNodeTarget>

    @Suppress("UnstableApiUsage")
    final override fun Project.applyPlugin() {
        apply {
            plugin("com.moowork.node")
        }

        val ext = extensions.create("kotlinJsNodeTests", extensionClass.java, this)

        extensions.configure<NodeExtension>("node") {
            version = "10.12.0"
            download = true
        }

        repositories.whenObjectAdded {
            (this as? IvyArtifactRepository)?.metadataSources { artifact() }
        }

        afterEvaluate {
            listTargets(ext).forEach { addKotlinJsTestTasks(this, it) }
        }
    }

    private fun addKotlinJsTestTasks(project: Project, target: KotlinJsTestsNodeTarget) {
        if (target.mainCompileTask.kotlinOptions.moduleKind !in arrayOf("commonjs", "umd")) {
            throw IllegalStateException("The moduleKind of the target \"${target.name}\" must be either \"commonjs\" or \"umd\" for tests to be runnable on NodeJS")
        }

        target.testCompileTask.kotlinOptions.moduleKind = "commonjs"

        val testDir = target.outputDir

        val engineName = target.engine.npmModules.first().capitalize()

        var needed by Delegates.notNull<Boolean>()

        val checkSources = project.task<Task>("${target.name}TestCheckSources") {
            dependsOn(target.testCompileTask)
            doLast {
                needed = project.file(target.testCompileTask.outputFile).exists()
            }
        }

        val copyModules = project.task<Copy>("${target.name}TestCopyModules") {
            dependsOn(checkSources)
            onlyIf { needed }
            group = "build"
            into("$testDir/node_modules")
        }

        val prepareModules = project.task<Task>("${target.name}TestPrepareModules") {
            dependsOn(checkSources)
            onlyIf { needed }
            group = "build"
            dependsOn(target.testCompileTask)
            doLast {
                val classPath = target.testCompileTask.classpath + target.testCompileTask.outputFile.parentFile

                classPath
                        .filter { !it.isFile }
                        .forEach {
                            copyModules.from(project.fileTree(it.absolutePath).matching {
                                include {
                                    !it.path.startsWith("META-INF/") || it.path.startsWith("META-INF/resources/")
                                }
                            })
                        }

                classPath
                        .filter { it.isFile }
                        .forEach {
                            copyModules.from(project.zipTree(it.absolutePath).matching {
                                include {
                                    !it.path.startsWith("META-INF/") || it.path.startsWith("META-INF/resources/")
                                }
                            })
                        }
            }
        }

        copyModules.dependsOn(prepareModules)

        val installRunner = project.task<NpmTask>("${target.name}TestInstall$engineName") {
            dependsOn(checkSources)
            onlyIf { needed }
            group = "node"
            setArgs(listOf("install") + target.engine.npmModules)
            setWorkingDir(project.file(testDir))
        }

        val runTests = project.task<NodeTask>("${target.name}TestRun$engineName") {
            dependsOn(checkSources)
            onlyIf { needed }
            group = "verification"
            setWorkingDir(project.file(testDir))
            setScript(project.file("$testDir/node_modules/${target.engine.executable}"))
            setArgs(target.engine.firstArguments + listOf("node_modules/" + project.file(target.testCompileTask.outputFile).name))
        }

        runTests.dependsOn(copyModules, installRunner)

        val testTask = project.tasks.findByName("${target.name}Test") ?: project.tasks.getByName("test")
        testTask.dependsOn(runTests)
    }

}
