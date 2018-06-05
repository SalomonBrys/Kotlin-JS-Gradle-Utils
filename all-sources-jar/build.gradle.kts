plugins {
    kotlin("jvm")
    `java-library`
    `kotlin-dsl`
    id("com.jfrog.bintray")
}

val kotlinVer: String by rootProject.extra

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation(kotlin("stdlib", kotlinVer))
    implementation("com.android.tools.build:gradle:3.1.2")
}

mavenPublish()
