package com.github.salomonbrys.gradle.kjs.jstests

import com.moowork.gradle.node.npm.NpmTask
import com.moowork.gradle.node.task.NodeTask
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.task
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

fun KotlinTarget.addKotlinJSTest(engine: KotlinJsTests.Engine = KotlinJsTests.Mocha) {
    val testCompile = testJsCompileTask
    testCompile.kotlinOptions.moduleKind = "commonjs"

    val testDir = "${project.buildDir}/test-js/$name"

    val nodeModules = project.task<Copy>("${name}TestNodeModules") {
        dependsOn(testCompile)
        into("$testDir/node_modules")
    }

    project.afterEvaluate {
        val classPath = testCompile.classpath + testCompile.outputFile.parentFile

        classPath
                .filter { !it.isFile }
                .forEach {
                    nodeModules.from(fileTree(it.absolutePath).matching {
                        include {
                            !it.path.startsWith("META-INF/") || it.path.startsWith("META-INF/resources/")
                        }
                    })
                }

        classPath
                .filter { it.isFile }
                .forEach {
                    nodeModules.from(zipTree(it.absolutePath).matching {
                        include {
                            !it.path.startsWith("META-INF/") || it.path.startsWith("META-INF/resources/")
                        }
                    })
                }
    }

    val engineName = engine.npmModules.first().capitalize()

    val installRunner = project.task<NpmTask>("${name}TestInstall$engineName") {
        setArgs(listOf("install") + engine.npmModules)
        setWorkingDir(project.file(testDir))
    }

    val runTests = project.task<NodeTask>("${name}TestRun$engineName") {
        dependsOn(testCompile, nodeModules, installRunner)
        setWorkingDir(project.file(testDir))
        setScript(project.file("$testDir/node_modules/${engine.executable}"))
        setArgs(engine.firstArguments + listOf("node_modules/" + project.file(testCompile.outputFile).name))
    }

    project.tasks["${name}Test"].dependsOn(runTests)
}
