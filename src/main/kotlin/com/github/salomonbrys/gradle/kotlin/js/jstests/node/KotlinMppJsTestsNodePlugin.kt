package com.github.salomonbrys.gradle.kotlin.js.jstests.node

import com.github.salomonbrys.gradle.kotlin.js.jscompiletasks.KotlinMppJsCompileTasksPlugin

class KotlinMppJsTestsNodePlugin : KotlinJsTestsNodePlugin<KotlinMppJsTestsNodeExtension>() {

    override val extensionClass get() = KotlinMppJsTestsNodeExtension::class

    override val tasksPlugin get() = KotlinMppJsCompileTasksPlugin::class

    override fun listTargets(ext: KotlinMppJsTestsNodeExtension) = ext.targets
}
