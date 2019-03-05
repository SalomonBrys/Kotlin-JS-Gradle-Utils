package com.github.salomonbrys.gradle.kotlin.js.jstests.node

class KotlinMppJsTestsNodePlugin : KotlinJsTestsNodePlugin<KotlinMppJsTestsNodeExtension>() {

    override val extensionClass get() = KotlinMppJsTestsNodeExtension::class

    override fun listTargets(ext: KotlinMppJsTestsNodeExtension) = ext.targets
}
