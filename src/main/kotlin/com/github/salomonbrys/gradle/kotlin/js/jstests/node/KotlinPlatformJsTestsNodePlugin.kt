package com.github.salomonbrys.gradle.kotlin.js.jstests.node

class KotlinPlatformJsTestsNodePlugin : KotlinJsTestsNodePlugin<KotlinPlatformJsTestsNodeExtension>() {

    override val extensionClass get() = KotlinPlatformJsTestsNodeExtension::class

    override fun listTargets(ext: KotlinPlatformJsTestsNodeExtension) = listOf(ext)
}
