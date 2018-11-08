package com.github.salomonbrys.gradle.kjs.jstests

import org.gradle.api.Project
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

class KotlinJsTests(val project: Project) {

    data class Engine(val npmModules: List<String>, val executable: String, val firstArguments: List<String> = emptyList()) {
        companion object {
            @JvmStatic val Jasmine = Engine(listOf("jasmine"), "jasmine/bin/jasmine.js")
            @JvmStatic val Jest = Engine(listOf("jest"), "jest/bin/jest.js")
            @JvmStatic val Mocha = Engine(listOf("mocha"), "mocha/bin/mocha")
            @JvmStatic val QUnit = Engine(listOf("qunit"), "qunit/bin/qunit")
            @JvmStatic val Tape = Engine(listOf("tape"), "tape/bin/tape", listOf("tape-plugin.js"))
        }
    }

    val Jasmine get() = Engine.Jasmine
    val Jest get() = Engine.Jest
    val Mocha get() = Engine.Mocha
    val QUnit get() = Engine.QUnit
    val Tape get() = Engine.Tape

    @JvmOverloads
    fun addKJSTestTasks(targetName: String, engine: Engine = Mocha) {
        val kotlin = project.extensions["kotlin"] as KotlinMultiplatformExtension
        val target = kotlin.targets[targetName]
        target.addKotlinJSTest(engine)
    }

    @JvmOverloads
    fun KotlinTarget.addKJSTestTasks(engine: Engine = Mocha) = addKotlinJSTest(engine)
}
