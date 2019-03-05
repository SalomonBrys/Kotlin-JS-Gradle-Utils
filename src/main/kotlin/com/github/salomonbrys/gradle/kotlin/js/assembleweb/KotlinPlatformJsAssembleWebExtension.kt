package com.github.salomonbrys.gradle.kotlin.js.assembleweb

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

open class KotlinPlatformJsAssembleWebExtension(project: Project) :
        KotlinJsWebTarget("js", project.tasks.getByName<Kotlin2JsCompile>("compileKotlin2Js"), listOf("compileClasspath", "runtimeClasspath"), "${project.buildDir}/web/js"),
        KotlinJsAssembleWebExtension
