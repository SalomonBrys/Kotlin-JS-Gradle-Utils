package com.github.salomonbrys.gradle.kotlin.js.jstests.node

interface KotlinJsTestsNodeExtension {

    val jasmine get() = Engine.jasmine
    val jest get() = Engine.jest
    val mocha get() = Engine.mocha
    val qUnit get() = Engine.qUnit
    val tape get() = Engine.tape

}
