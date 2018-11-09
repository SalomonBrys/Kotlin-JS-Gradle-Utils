package com.github.salomonbrys.gradle.kotlin.js.jstests.node

import org.gradle.api.Named
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile


open class KotlinJsTestsNodeTarget(private val name: String, val mainCompileTask: Kotlin2JsCompile, val testCompileTask: Kotlin2JsCompile) : Named {
    override fun getName() = name

    var engine: Engine = Engine.default

}
