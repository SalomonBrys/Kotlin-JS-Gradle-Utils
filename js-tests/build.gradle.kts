
plugins {
    kotlin("jvm")
    `kotlin-dsl`
    id("com.jfrog.bintray")
}

val kotlinVer: String by rootProject.extra

dependencies {
    compile(gradleApi())
    gradleKotlinDsl()
    compile(kotlin("stdlib", kotlinVer))
    compile("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVer")
    compile("gradle.plugin.de.solugo.gradle:gradle-nodejs-plugin:0.2.1")
}

mavenPublish()
