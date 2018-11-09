package com.github.salomonbrys.gradle.kotlin.js.jstests.node

data class Engine(val npmModules: List<String>, val executable: String, val firstArguments: List<String> = emptyList()) {
    companion object {
        val jasmine = Engine(listOf("jasmine"), "jasmine/bin/jasmine.js")
        val jest = Engine(listOf("jest"), "jest/bin/jest.js")
        val mocha = Engine(listOf("mocha"), "mocha/bin/mocha")
        val qUnit = Engine(listOf("qunit"), "qunit/bin/qunit")
        val tape = Engine(listOf("tape"), "tape/bin/tape", listOf("tape-plugin.js"))

        @JvmStatic val default get() = mocha
    }
}
