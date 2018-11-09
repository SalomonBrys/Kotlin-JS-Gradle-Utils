
Kotlin/JS gradle utilities
==========================

Collection of Gradle plugins to ease the use & test of Kotlin/JS code.

- The **js-tests.node** plugin enables you to run Kotin/JS tests with NodeJS.
- The **assemble-web** plugin enables you to copy compiled Kotlin/JS and dependencies JS files to a web directory.
- The **js-compile-tasks** plugin enables you to easily access compile tasks in Kotlin DSL.

## Install

#### Gradle Groovy DSL

Add the bintray repository & plugin classpath to your buildscript:

```groovy
// build.gradle

buildscript {
    repositories {
        maven { url "https://dl.bintray.com/salomonbrys/gradle-plugins" }
    }
    dependencies {
        classpath "com.github.salomonbrys.gradle.kotlin.js:kotlin-js-gradle-utils:1.0.0"
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
        maven(url = "https://dl.bintray.com/salomonbrys/gradle-plugins")
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("com.github.salomonbrys.gradle.kotlin.js.")) {
                useModule("com.github.salomonbrys.gradle.kotlin.js:kotlin-js-gradle-utils:1.0.0")
            }
        }
    }
}
```

## Kotlin/JS tests on NodeJS

This plugin allows you to run Kotlin/JS tests with NodeJS using various runners (default is Mocha).
Multiple test engines are supported.

This plugin uses [gradle-node-plugin](https://github.com/srs/gradle-node-plugin), which means:

- NodeJS is downloaded by Gradle (unless configured not to)
- You can set all node/npm related [configurations](https://github.com/srs/gradle-node-plugin/blob/master/docs/node.md#configuring-the-plugin).

### In multiplatform modules

#### Install

##### Gradle Groovy DSL

```groovy
apply plugin: "com.github.salomonbrys.gradle.kotlin.js.mpp-tests.node"
```

##### Gradle Kotlin DSL

```kotlin
plugins {
    id("com.github.salomonbrys.gradle.kotlin.js.mpp-tests.node")
}
```

#### Pre-requisite

The JS target containing code must be compiled either as "umd" or "commonjs" module ("umd" is recommended).

##### Gradle Groovy DSL

```groovy
kotlin {
    targets {
        fromPreset(presets.js, 'js') {
            tasks[compilations.main.compileKotlinTaskName].kotlinOptions.moduleKind = "umd"
        }
    }
}
```

##### Kotlin Groovy DSL

```kotlin
kotlin {
    targets {
        add(presets["js"].createTarget("js").apply {
            kotlinJsCompileTasks {
                main.kotlinOptions.moduleKind = "umd"
            }
        })
    }
}
```

#### Use

##### Gradle Groovy DSL

For the plugin to create the test tasks for your target, use the `kotlinJsNodeTests` extension:

```groovy
kotlinJsNodeTests {
    js
}
```

You can configure which engine is used (Mocha by default):

```groovy
kotlinJsNodeTests {
    js {
        engine = qUnit
    }
}
```

Valid values are `jasmine`, `jest`, `mocha`, `qUnit`, `tape`.


##### Gradle Kotlin DSL

For the plugin to create the test tasks for your target, use the `kotlinJsNodeTests` extension with Kotlin extension functions:

```kotlin
kotlin {
    targets {
        add(presets["js"].createTarget("js").apply {
            kotlinJsNodeTests { thisTarget() }
        })
    }
}
```

You can configure which engine is used (Mocha by default):

```kotlin
kotlin {
    targets {
        add(presets["js"].createTarget("js").apply {
            kotlinJsNodeTests {
                thisTarget {
                    engine = mocha
                }
            }
        })
    }
}
```

Valid values are `jasmine`, `jest`, `mocha`, `qUnit`, `tape`.

You can also use the top-level API (without Kotlin extension functions):

```kotlin
kotlinJsNodeTests {
    targets {
        create("js")
    }
}

// OR

kotlinJsNodeTests {
    targets {
        create("js") {
            engine = mocha
        }
    }
}
```

### In JS-only modules

#### Install

##### Gradle Groovy DSL

```groovy
apply plugin: "com.github.salomonbrys.gradle.kotlin.js.platform-tests.node"
```

##### Gradle Kotlin DSL

```kotlin
plugins {
    id("com.github.salomonbrys.gradle.kotlin.js.platform-tests.node")
}
```

#### Pre-requisite

The JS target containing code must be compiled either as "umd" or "commonjs" module ("umd" is recommended).

##### Gradle Groovy DSL

```groovy
compileKotlin2Js.kotlinOptions.moduleKind = "umd"
```

##### Gradle Kotlin DSL

```kotlin
kotlinJsCompileTasks.compileKotlin2Js.kotlinOptions.moduleKind = "umd"
```

#### Use

Simply applying the plugin will create the test tasks for the current module.

You can configure the engine used for the tests (default is Mocha).
The API is the same for Gradle Groovy and Kotlin DSLs:

```kotlin
kotlinJsNodeTests {
    engine = mocha
}
```

Valid values are `jasmine`, `jest`, `mocha`, `qUnit`, `tape`.


## Kotlin/JS Assemble Web

This plugin allows you to copy all compiled and dependencies JS files to a web directory.

### In multiplatform modules

#### Install

##### Gradle Groovy DSL

```groovy
apply plugin: "com.github.salomonbrys.gradle.kotlin.js.mpp-assemble-web"
```

##### Gradle Kotlin DSL

```kotlin
plugins {
    id("com.github.salomonbrys.gradle.kotlin.js.mpp-assemble-web")
}
```

#### Use

##### Gradle Groovy DSL

For the plugin to create the sync tasks for your target, use the `assembleWeb` extension:

```groovy
assembleWeb {
    js
}
```

You can configure the output directory (default is `"$buildDir/web/${target.name}"`):

```groovy
assembleWeb {
    js {
        outputDir = "$projectDir/web/KotlinJS"
    }
}
```

##### Gradle Kotlin DSL

For the plugin to create the sync tasks for your target, use the `assembleWeb` extension with Kotlin extension functions:

```kotlin
kotlin {
    targets {
        add(presets["js"].createTarget("js").apply {
            assembleWeb { thisTarget() }
        })
    }
}
```

You can configure the output directory (default is `"$buildDir/web/${target.name}"`):

```kotlin
kotlin {
    targets {
        add(presets["js"].createTarget("js").apply {
            assembleWeb {
                thisTarget {
                    outputDir = "$projectDir/web/KotlinJS"
                }
            }
        })
    }
}
```

You can also use the top-level API (without Kotlin extension functions):

```kotlin
assembleWeb {
    targets {
        create("js")
    }
}

// OR

assembleWeb {
    targets {
        create("js") {
            outputDir = "$projectDir/web/KotlinJS"
        }
    }
}
```

### In JS-only modules

#### Install

##### Gradle Groovy DSL

```groovy
apply plugin: "com.github.salomonbrys.gradle.kotlin.js.platform-assemble-web"
```

##### Gradle Kotlin DSL

```kotlin
plugins {
    id("com.github.salomonbrys.gradle.kotlin.js.platform-assemble-web")
}
```

#### Use

Simply applying the plugin will create the sync tasks for the current module.

You can configure the output directory (default is `"$buildDir/web/${target.name}"`).
The API is the same for Gradle Groovy and Kotlin DSLs:

```kotlin
assembleWeb {
    outputDir = "$buildDir/webapp/js"
}
```


## Kotlin/JS compile tasks

This plugin enables you to easily access Kotlin/JS compile tasks to configure it **in Gradle Kotlin DSL**.
This plugin provides nothing interesting when using Gradle Groovy DSL.
**It is enabled by all other plugins of this project**, so you don't have to manually install it if you are using any of them.

### In multiplatform modules

#### Install

```kotlin
plugins {
    id("com.github.salomonbrys.gradle.kotlin.js.mpp-compile-tasks")
}
```

#### Use

To access the Kotlin/JS compile task for your target, use the `kotlinJsCompileTasks` extension with Kotlin extension functions:

```kotlin
kotlin {
    targets {
        add(presets["js"].createTarget("js").apply {
            kotlinJsCompileTasks {
                main.kotlinOptions.moduleKind = "umd"
                test.kotlinOptions.moduleKind = "commonjs"
            }
        })
    }
}
```

You can also use the top-level API (without Kotlin extension functions):

```kotlin
kotlinJsCompileTasks.of("js").main.kotlinOptions.moduleKind = "umd"
kotlinJsCompileTasks.of("js").test.kotlinOptions.moduleKind = "commonjs"
```

### In JS-only modules

#### Install

```kotlin
// build.gradle.kts

plugins {
    id("com.github.salomonbrys.gradle.kotlin.js.platform-compile-tasks")
}
```

#### Use

To access the Kotlin/JS compile task, use the `kotlinJsCompileTasks` extension:

```kotlin
kotlinJsCompileTasks.compileKotlin2Js.kotlinOptions.moduleKind = "umd"
kotlinJsCompileTasks.compileTestKotlin2Js.kotlinOptions.moduleKind = "commonjs"
```
