package com.github.salomonbrys.gradle.kotlin.js.assembleweb

class KotlinMppJsAssembleWebPlugin : KotlinJsAssembleWebPlugin<KotlinMppJsAssembleWebExtension>() {

    override val extensionClass get() = KotlinMppJsAssembleWebExtension::class

    override fun listTargets(ext: KotlinMppJsAssembleWebExtension) = ext.targets

}
