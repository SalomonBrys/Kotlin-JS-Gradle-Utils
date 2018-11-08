package com.github.salomonbrys.gradle.kjs.jstests

import com.github.salomonbrys.gradle.kjs.jscompiletasks.mainJsCompileTask
import com.github.salomonbrys.gradle.kjs.jscompiletasks.testJsCompileTask
import com.moowork.gradle.node.npm.NpmTask
import com.moowork.gradle.node.task.NodeTask
import org.gradle.api.Task
import org.gradle.api.internal.AbstractTask
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.task
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import java.lang.IllegalStateException

internal fun KotlinTarget.addKotlinJSTest(engine: KotlinJsTests.Engine) {
    if (mainJsCompileTask.kotlinOptions.moduleKind !in arrayOf("commonjs", "umd")) {
        throw IllegalStateException("The moduleKind of the target \"$name\" must be either \"commonjs\" or \"umd\" for tests to be runnable on NodeJS")
    }

    val testCompile = testJsCompileTask
    testCompile.kotlinOptions.moduleKind = "commonjs"

    val testDir = "${project.buildDir}/test-js/$name"

    val copyModules = project.task<Copy>("${name}TestCopyModules") {
        group = "build"
        into("$testDir/node_modules")
    }

    val prepareModules = project.task<Task>("${name}TestPrepareModules") {
        group = "build"
        dependsOn(testCompile)
        doLast {
            val classPath = testCompile.classpath + testCompile.outputFile.parentFile

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

    val engineName = engine.npmModules.first().capitalize()

    val installRunner = project.task<NpmTask>("${name}TestInstall$engineName") {
        group = "node"
        setArgs(listOf("install") + engine.npmModules)
        setWorkingDir(project.file(testDir))
    }

    val runTests = project.task<NodeTask>("${name}TestRun$engineName") {
        group = "verification"
        setWorkingDir(project.file(testDir))
        setScript(project.file("$testDir/node_modules/${engine.executable}"))
        setArgs(engine.firstArguments + listOf("node_modules/" + project.file(testCompile.outputFile).name))
    }

    val checkSources = project.task<Task>("${name}TestCheckSources") {
        dependsOn(testCompile)
        doLast {
            if (project.file(testCompile.outputFile).exists().not())
                listOf(copyModules, installRunner, runTests, project.tasks["nodeSetup"] as AbstractTask).forEach { it.isEnabled = false }
        }
    }

    runTests.dependsOn(checkSources, copyModules, installRunner)

    project.tasks["${name}Test"].dependsOn(runTests)
}
