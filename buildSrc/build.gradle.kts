plugins {
    kotlin("jvm") version "1.2.41"
    `kotlin-dsl`
}

repositories {
    jcenter()
}

dependencies {
    compile(gradleApi())
    gradleKotlinDsl()
    compile(kotlin("stdlib", "1.2.41"))
    compile("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.0")
}
