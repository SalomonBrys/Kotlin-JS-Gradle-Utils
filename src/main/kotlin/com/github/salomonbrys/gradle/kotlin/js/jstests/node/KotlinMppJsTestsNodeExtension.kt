package com.github.salomonbrys.gradle.kotlin.js.jstests.node

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.internal.plugins.DslObject
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinJsOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinOnlyTarget

open class KotlinMppJsTestsNodeExtension(project: Project) : KotlinJsTestsNodeExtension {

    private fun createTestsTargetFrom(target: KotlinOnlyTarget<KotlinJsCompilation>) = KotlinJsTestsNodeTarget(target.name, target.compilations["main"].compileKotlinTask, target.compilations["test"].compileKotlinTask)

    val targets = project.container(KotlinJsTestsNodeTarget::class.java) {
        val kotlin = project.extensions["kotlin"] as KotlinMultiplatformExtension

        @Suppress("UNCHECKED_CAST")
        val target = (kotlin.targets.findByName(it) ?: throw IllegalArgumentException("Could not find Kotlin target $it")) as KotlinOnlyTarget<KotlinJsCompilation>
        createTestsTargetFrom(target)
    }

    init {
        @Suppress("LeakingThis")
        DslObject(this).extensions.add("targets", targets)
    }

    @JvmOverloads
    fun KotlinOnlyTarget<KotlinJsCompilation>.thisTarget(action: Action<KotlinJsTestsNodeTarget> = Action {}) {
        val target = createTestsTargetFrom(this)
        action.execute(target)
        targets.add(target)
    }

}
