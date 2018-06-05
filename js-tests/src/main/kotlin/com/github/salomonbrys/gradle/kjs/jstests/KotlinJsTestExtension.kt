package com.github.salomonbrys.gradle.kjs.jstests

class KotlinJsTestExtension {

    data class Engine(val npmModules: List<String>, val executable: String, val firstArguments: List<String> = emptyList())

    val Jasmine = Engine(listOf("jasmine"), "node_modules/jasmine/bin/jasmine.js")
    val Jest = Engine(listOf("jest"), "node_modules/jest/bin/jest.js")
    val Mocha = Engine(listOf("mocha"), "node_modules/mocha/bin/mocha")
    val QUnit = Engine(listOf("qunit"), "node_modules/qunit/bin/qunit")
    val Tape = Engine(listOf("tape"), "node_modules/tape/bin/tape", listOf("tape-plugin.js"))

    var engine: Engine = QUnit
}
