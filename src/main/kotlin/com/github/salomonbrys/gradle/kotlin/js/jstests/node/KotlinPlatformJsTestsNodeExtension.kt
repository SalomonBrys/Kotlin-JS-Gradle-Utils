package com.github.salomonbrys.gradle.kotlin.js.jstests.node

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

open class KotlinPlatformJsTestsNodeExtension(project: Project) :
        KotlinJsTestsNodeTarget("js", project.tasks.getByName<Kotlin2JsCompile>("compileKotlin2Js"), project.tasks.getByName<Kotlin2JsCompile>("compileTestKotlin2Js")),
        KotlinJsTestsNodeExtension
