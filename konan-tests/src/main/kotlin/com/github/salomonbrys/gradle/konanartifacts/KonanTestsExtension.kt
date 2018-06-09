package com.github.salomonbrys.gradle.konanartifacts

import groovy.lang.Closure
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.task
import org.jetbrains.kotlin.gradle.plugin.KonanArtifactContainer
import org.jetbrains.kotlin.gradle.plugin.KonanProgram

class KonanTestsExtension(private val project: Project) {

    @JvmOverloads
    fun testProgram(libName: String, action: KonanProgram.() -> Unit = {}) {
        project.extensions.configure<KonanArtifactContainer>("konanArtifacts") {
            program("${libName}Test") {
                srcDir("test")
                commonSourceSets("test")
                libraries {
                    artifact(libName)
                }
                extraOpts("-tr")
                this.action()
            }
        }

        val testTask = project.tasks.findByName("test") ?: project.task("test") { group = "verification" }
        testTask.dependsOn(project.tasks["run${libName.capitalize()}Test"])
    }

    fun testProgram(libName: String, action: Action<KonanProgram>) = testProgram(libName) { action. execute(this) }

    fun testProgram(libName: String, action: Closure<*>) = testProgram(libName) {
        action.delegate = this
        action.run()
    }

}
