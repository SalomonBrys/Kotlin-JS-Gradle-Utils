plugins {
    groovy
    kotlin("jvm")
    `kotlin-dsl`
    id("com.jfrog.bintray")
}

val kotlinVer: String by rootProject.extra
val konanVer: String by rootProject.extra

dependencies {
    compile(gradleApi())
    gradleKotlinDsl()
    compile(kotlin("stdlib", kotlinVer))
    compile("org.jetbrains.kotlin:kotlin-native-gradle-plugin:$konanVer")
}

mavenPublish()

val compileKotlin: AbstractCompile by tasks
val compileGroovy: AbstractCompile by tasks

compileKotlin.dependsOn(compileGroovy)
compileGroovy.dependsOn.remove("compileJava")
compileKotlin.classpath += files(compileGroovy.destinationDir)
