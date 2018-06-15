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
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVer")
    implementation("com.moowork.gradle:gradle-node-plugin:1.2.0")
}

mavenPublish()
