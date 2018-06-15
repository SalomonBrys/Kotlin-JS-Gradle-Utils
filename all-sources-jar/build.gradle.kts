plugins {
    kotlin("jvm")
    `java-library`
    `kotlin-dsl`
}

val kotlinVer: String by rootProject.extra

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation(kotlin("stdlib", kotlinVer))
    implementation("com.android.tools.build:gradle:3.1.3")
}

mavenPublish()
