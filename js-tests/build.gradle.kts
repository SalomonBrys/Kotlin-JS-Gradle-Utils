
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
    compile("com.moowork.gradle:gradle-node-plugin:1.2.0")
}

mavenPublish()
