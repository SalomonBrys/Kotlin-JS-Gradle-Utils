package com.github.salomonbrys.gradle.kotlin.js.jstests.node

import com.github.salomonbrys.gradle.kotlin.js.jscompiletasks.mainJsCompileTask
import com.github.salomonbrys.gradle.kotlin.js.jscompiletasks.testJsCompileTask
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.internal.plugins.DslObject
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

open class KotlinMppJsTestsNodeExtension(project: Project) : KotlinJsTestsNodeExtension {

    private fun createTestsTargetFrom(target: KotlinTarget) = KotlinJsTestsNodeTarget(target.name, target.mainJsCompileTask, target.testJsCompileTask)

    val targets = project.container(KotlinJsTestsNodeTarget::class.java) {
        val kotlin = project.extensions["kotlin"] as KotlinMultiplatformExtension
        val target = kotlin.targets[it]
        createTestsTargetFrom(target)
    }

    init {
        @Suppress("LeakingThis")
        DslObject(this).extensions.add("targets", targets)
    }

    @JvmOverloads
    fun KotlinTarget.thisTarget(action: Action<KotlinJsTestsNodeTarget> = Action {}) {
        val target = createTestsTargetFrom(this)
        action.execute(target)
        targets.add(target)
    }

}
