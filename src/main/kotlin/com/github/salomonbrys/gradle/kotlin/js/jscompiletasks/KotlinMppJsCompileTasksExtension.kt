package com.github.salomonbrys.gradle.kotlin.js.jscompiletasks

import org.gradle.api.Project
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

open class KotlinMppJsCompileTasksExtension(private val project: Project) {

    val KotlinTarget.main get() = mainJsCompileTask
    val KotlinTarget.test get() = testJsCompileTask

    class CompileTasks(private val target: KotlinTarget) {
        val main get() = target.mainJsCompileTask
        val test get() = target.testJsCompileTask
    }

    fun of(target: KotlinTarget) = CompileTasks(target)
    fun of(name: String) = CompileTasks((project.extensions["kotlin"] as KotlinMultiplatformExtension).targets[name])
}
