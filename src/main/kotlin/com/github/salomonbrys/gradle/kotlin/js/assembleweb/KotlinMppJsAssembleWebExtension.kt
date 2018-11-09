package com.github.salomonbrys.gradle.kotlin.js.assembleweb

import com.github.salomonbrys.gradle.kotlin.js.jscompiletasks.mainJsCompileTask
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.internal.plugins.DslObject
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import java.lang.IllegalArgumentException

open class KotlinMppJsAssembleWebExtension(val project: Project) : KotlinJsAssembleWebExtension {

    private fun createWebTargetFrom(target: KotlinTarget) = KotlinJsWebTarget(target.name, target.mainJsCompileTask, listOf(target.defaultConfigurationName), "${project.buildDir}/web/${target.name}")

    val targets = project.container(KotlinJsWebTarget::class.java) {
        val kotlin = project.extensions.getByName("kotlin") as KotlinMultiplatformExtension
        val target = kotlin.targets.findByName(it) ?: throw IllegalArgumentException("Could not fined Kotlin target $it")
        createWebTargetFrom(target)
    }

    init {
        @Suppress("LeakingThis")
        DslObject(this).extensions.add("targets", targets)
    }

    @JvmOverloads
    fun KotlinTarget.thisTarget(action: Action<KotlinJsWebTarget> = Action {}) {
        val target = createWebTargetFrom(this)
        action.execute(target)
        targets.add(target)
    }

}
