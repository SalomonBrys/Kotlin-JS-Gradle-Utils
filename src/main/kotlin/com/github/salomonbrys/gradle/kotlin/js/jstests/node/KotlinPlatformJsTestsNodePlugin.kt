package com.github.salomonbrys.gradle.kotlin.js.jstests.node

import com.github.salomonbrys.gradle.kotlin.js.jscompiletasks.KotlinPlatformJsCompileTasksPlugin

class KotlinPlatformJsTestsNodePlugin : KotlinJsTestsNodePlugin<KotlinPlatformJsTestsNodeExtension>() {

    override val extensionClass get() = KotlinPlatformJsTestsNodeExtension::class

    override val tasksPlugin get() = KotlinPlatformJsCompileTasksPlugin::class

    override fun listTargets(ext: KotlinPlatformJsTestsNodeExtension) = listOf(ext)
}
