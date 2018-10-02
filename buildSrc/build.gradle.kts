plugins {
    kotlin("jvm") version "1.2.31"
    `kotlin-dsl`
}

repositories {
    jcenter()
}

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation(kotlin("stdlib", "1.2.31"))
    implementation("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.0")
}
