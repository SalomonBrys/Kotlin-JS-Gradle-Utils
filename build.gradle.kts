
extra["kotlinVer"] = "1.2.41"
extra["konanVer"] = "0.6.2"

plugins {
    kotlin("jvm") version "1.2.41" apply false
}

allprojects {
    group = "com.github.salomonbrys.gradle"
    version = "1.0.3"
}

subprojects {
    repositories {
        jcenter()
        google()
        maven(url = "https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
        maven(url = "https://plugins.gradle.org/m2/")
    }

}
