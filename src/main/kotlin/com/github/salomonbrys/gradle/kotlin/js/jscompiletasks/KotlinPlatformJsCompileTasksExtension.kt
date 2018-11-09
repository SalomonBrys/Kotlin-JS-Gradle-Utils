package com.github.salomonbrys.gradle.kotlin.js.jscompiletasks

import org.gradle.api.Action
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

open class KotlinPlatformJsCompileTasksExtension(private val project: Project) {

    val compileKotlin2Js get() = project.compileKotlin2Js
    fun compileKotlin2Js(action: Action<Kotlin2JsCompile>) = action.execute(project.compileKotlin2Js)
    val compileTestKotlin2Js get() = project.compileTestKotlin2Js
    fun compileTestKotlin2Js(action: Action<Kotlin2JsCompile>) = action.execute(project.compileTestKotlin2Js)
}
