
extra["kotlinVer"] = "1.2.31"
extra["konanVer"] = "0.6.2"

plugins {
    kotlin("jvm") version "1.2.31" apply false
}

allprojects {
    group = "com.github.salomonbrys.gradle"
    version = "1.0.2"
}

subprojects {
    repositories {
        jcenter()
        google()
        maven(url = "https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
        maven(url = "https://plugins.gradle.org/m2/")
    }

}
