package com.github.salomonbrys.gradle.kotlin.js.assembleweb

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.internal.plugins.DslObject
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinOnlyTarget

open class KotlinMppJsAssembleWebExtension(val project: Project) : KotlinJsAssembleWebExtension {

    private fun createWebTargetFrom(target: KotlinOnlyTarget<KotlinJsCompilation>) = KotlinJsWebTarget(target.name, target.compilations["main"].compileKotlinTask, listOf(target.defaultConfigurationName), "${project.buildDir}/web/${target.name}")

    val targets = project.container(KotlinJsWebTarget::class.java) {
        val kotlin = project.extensions.getByName("kotlin") as KotlinMultiplatformExtension
        @Suppress("UNCHECKED_CAST")
        val target = (kotlin.targets.findByName(it) ?: throw IllegalArgumentException("Could not find Kotlin target $it")) as KotlinOnlyTarget<KotlinJsCompilation>
        createWebTargetFrom(target)
    }

    init {
        @Suppress("LeakingThis")
        DslObject(this).extensions.add("targets", targets)
    }

    @JvmOverloads
    fun KotlinOnlyTarget<KotlinJsCompilation>.thisTarget(action: Action<KotlinJsWebTarget> = Action {}) {
        val target = createWebTargetFrom(this)
        action.execute(target)
        targets.add(target)
    }

}
