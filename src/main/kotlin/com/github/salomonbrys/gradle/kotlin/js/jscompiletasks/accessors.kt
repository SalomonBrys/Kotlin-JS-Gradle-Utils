package com.github.salomonbrys.gradle.kotlin.js.jscompiletasks

import org.gradle.api.Project
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

internal val KotlinTarget.mainJsCompileTask get() =
    project.tasks[compilations["main"].compileKotlinTaskName] as Kotlin2JsCompile

internal val KotlinTarget.testJsCompileTask get() =
    project.tasks[compilations["test"].compileKotlinTaskName] as Kotlin2JsCompile

internal val Project.compileKotlin2Js get() = tasks.getByName("compileKotlin2Js") as Kotlin2JsCompile

internal val Project.compileTestKotlin2Js get() = tasks.getByName("compileTestKotlin2Js") as Kotlin2JsCompile
