package com.github.salomonbrys.gradle.kjs.jstests

import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

val KotlinTarget.mainJsCompileTask get() =
    project.tasks[compilations["main"].compileKotlinTaskName] as Kotlin2JsCompile

val KotlinTarget.testJsCompileTask get() =
    project.tasks[compilations["test"].compileKotlinTaskName] as Kotlin2JsCompile

