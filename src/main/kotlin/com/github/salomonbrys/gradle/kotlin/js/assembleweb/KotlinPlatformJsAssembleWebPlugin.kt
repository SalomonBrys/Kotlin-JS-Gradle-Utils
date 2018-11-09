package com.github.salomonbrys.gradle.kotlin.js.assembleweb

class KotlinPlatformJsAssembleWebPlugin : KotlinJsAssembleWebPlugin<KotlinPlatformJsAssembleWebExtension>() {

    override val extensionClass get() = KotlinPlatformJsAssembleWebExtension::class

    override fun listTargets(ext: KotlinPlatformJsAssembleWebExtension) = listOf(ext)

}
