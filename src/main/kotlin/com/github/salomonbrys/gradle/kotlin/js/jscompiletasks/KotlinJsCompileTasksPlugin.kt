package com.github.salomonbrys.gradle.kotlin.js.jscompiletasks

import com.github.salomonbrys.gradle.kotlin.js.KtPlugin
import org.gradle.api.Project
import kotlin.reflect.KClass

abstract class KotlinJsCompileTasksPlugin : KtPlugin<Project> {

    protected abstract val extensionClass: KClass<out Any>

    final override fun Project.applyPlugin() {
        extensions.create("kotlinJsCompileTasks", extensionClass.java, this)
    }

}
