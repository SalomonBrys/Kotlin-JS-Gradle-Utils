package com.github.salomonbrys.gradle.kotlin.js.assembleweb

import com.github.salomonbrys.gradle.kotlin.js.jscompiletasks.compileKotlin2Js
import org.gradle.api.Project

open class KotlinPlatformJsAssembleWebExtension(project: Project) : KotlinJsWebTarget("js", project.compileKotlin2Js, listOf("compileClasspath", "runtimeClasspath"), "${project.buildDir}/web/js"), KotlinJsAssembleWebExtension
