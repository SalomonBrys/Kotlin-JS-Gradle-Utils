package com.github.salomonbrys.gradle.kjs.jscompiletasks

import org.gradle.api.Action
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

class JSCompileTasksExtension(private val project: Project) {

    val KotlinTarget.main get() = mainJsCompileTask
    val KotlinTarget.test get() = testJsCompileTask

    val compileKotlin2Js get() = project.compileKotlin2Js

    fun compileKotlin2Js(action: Action<Kotlin2JsCompile>) = action.execute(project.compileKotlin2Js)
}
