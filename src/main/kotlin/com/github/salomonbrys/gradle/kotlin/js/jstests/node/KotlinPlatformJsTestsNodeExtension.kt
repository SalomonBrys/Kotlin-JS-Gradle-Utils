package com.github.salomonbrys.gradle.kotlin.js.jstests.node

import com.github.salomonbrys.gradle.kotlin.js.jscompiletasks.compileKotlin2Js
import com.github.salomonbrys.gradle.kotlin.js.jscompiletasks.compileTestKotlin2Js
import org.gradle.api.Project

open class KotlinPlatformJsTestsNodeExtension(project: Project) : KotlinJsTestsNodeTarget("js", project.compileKotlin2Js, project.compileTestKotlin2Js), KotlinJsTestsNodeExtension
