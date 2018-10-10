
extra["kotlinVer"] = "1.3.0-rc-146"

plugins {
    kotlin("jvm") version "1.2.31" apply false
}

allprojects {
    group = "com.github.salomonbrys.gradle.kjs"
    version = "1.0.0"
}

subprojects {
    repositories {
        jcenter()
        google()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
    }

}
