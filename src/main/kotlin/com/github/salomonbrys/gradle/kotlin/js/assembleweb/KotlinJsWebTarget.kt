package com.github.salomonbrys.gradle.kotlin.js.assembleweb

import org.gradle.api.Named
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

open class KotlinJsWebTarget(
        private val name: String,
        val compileTask: Kotlin2JsCompile,
        val configurationNames: Iterable<String>,

        var outputDir: String
) : Named {
    override fun getName() = name
}
