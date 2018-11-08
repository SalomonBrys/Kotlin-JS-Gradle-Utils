
Kotlin/JS gradle utilities
==========================

Collection of Gradle plugins to ease the use & test of Kotlin/JS code.

- The **js-tests** plugin enables you to run Kotin/JS tests with NodeJS.
- The **assemble-web** plugin enables you to copy compiled Kotlin/JS and dependencies JS files to a web directory.

## Install

#### Standard Gradle (Groovy)

Add the bintray repository & plugin classpath to your buildscript:

```groovy
// build.gradle

buildscript {
    repositories {
        maven { url "https://dl.bintray.com/salomonbrys/KJS-Gradle-Utils" }
    }
    dependencies {
        classpath "com.github.salomonbrys.gradle.kjs:kotlin-js-gradle-utils:1.0.0"
    }
}
```

#### Gradle Kotlin DSL

Configure the plugin resolution strategy:

```kotlin
// settings.gradle.kts

pluginManagement {
    repositories {
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://dl.bintray.com/salomonbrys/KJS-Gradle-Utils")
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("com.github.salomonbrys.gradle.kjs.")) {
                useModule("com.github.salomonbrys.gradle.kjs:kotlin-js-gradle-utils:1.0.0")
            }
        }
    }
}
```

## Kotlin/JS tests

This plugin allows you to easily run Kotlin/JS tests with NodeJS.
Multiple test engines are supported.

### Install

#### Standard Gradle (Groovy)

```groovy
// build.gradle

apply plugin: "com.github.salomonbrys.gradle.kjs.js-tests"
```

#### Gradle Kotlin DSL

```kotlin
// build.gradle.kts

plugins {
    id("com.github.salomonbrys.gradle.kjs.js-tests")
}
```

