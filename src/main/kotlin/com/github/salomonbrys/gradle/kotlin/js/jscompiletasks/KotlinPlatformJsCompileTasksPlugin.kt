package com.github.salomonbrys.gradle.kotlin.js.jscompiletasks

import org.gradle.api.Project

class KotlinPlatformJsCompileTasksPlugin : KotlinJsCompileTasksPlugin() {

    override val extensionClass = KotlinPlatformJsCompileTasksExtension::class

}
