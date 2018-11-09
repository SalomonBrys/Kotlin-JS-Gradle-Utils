package com.github.salomonbrys.gradle.kotlin.js

import org.gradle.api.Plugin

internal interface KtPlugin<T> : Plugin<T> {
    fun T.applyPlugin()
    override fun apply(target: T) = target.applyPlugin()
}
