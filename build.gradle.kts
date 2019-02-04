import com.jfrog.bintray.gradle.BintrayExtension

plugins {
    id("kotlin-platform-jvm")
    `kotlin-dsl`
    id("maven-publish")
    id("com.jfrog.bintray") version "1.8.0"
}

group = "com.github.salomonbrys.gradle.kotlin.js"
version = "1.1.0"

repositories {
    jcenter()
    google()
    maven(url = "https://plugins.gradle.org/m2/")
    maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
}

val kotlinVersion = "1.3.20"

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("com.moowork.gradle:gradle-node-plugin:1.2.0")
}

val sourcesJar = task<Jar>("sourcesJar") {
    classifier = "sources"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

afterEvaluate {
    sourcesJar.from(sourceSets["main"].allSource)
}

publishing {
    publications {
        create<MavenPublication>("Maven") {
            from(components["java"])
            artifact(sourcesJar)
        }
    }
}

if (hasProperty("bintrayUsername") && hasProperty("bintrayApiKey")) {
    val bintrayUsername: String by project
    val bintrayApiKey: String by project

    extensions.configure<BintrayExtension>("bintray") {
        user = bintrayUsername
        key = bintrayApiKey

        pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
            repo = "gradle-plugins"
            name = project.name
            setLicenses("MIT")
            websiteUrl = "https://github.com/SalomonBrys/Kotlin-JS-Gradle-Utils"
            issueTrackerUrl = "https://github.com/SalomonBrys/Kotlin-JS-Gradle-Utils/issues"
            vcsUrl = "https://github.com/SalomonBrys/Kotlin-JS-Gradle-Utils.git"

            setPublications("Maven")
        })

    }
}
