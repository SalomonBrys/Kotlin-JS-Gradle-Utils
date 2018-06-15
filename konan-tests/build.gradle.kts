plugins {
    kotlin("jvm")
    `java-library`
    `kotlin-dsl`
}

val kotlinVer: String by rootProject.extra
val konanVer: String by rootProject.extra

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation(kotlin("stdlib", kotlinVer))
    implementation("org.jetbrains.kotlin:kotlin-native-gradle-plugin:$konanVer")
}

mavenPublish()
