package com.github.salomonbrys.gradle.kmp.allsourcesjar

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.file.FileTree
import org.gradle.api.internal.artifacts.dependencies.DefaultProjectDependency
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getPluginByName
import org.gradle.kotlin.dsl.task

class AllSourcesJarPlugin : Plugin<Project> {

    private val Project.sourceSets: SourceSetContainer
        get() {
            val java = convention.getPluginByName<org.gradle.api.plugins.JavaPluginConvention>("java")
            return java.sourceSets
        }

    private fun Project.applyPlugin() {
        val sourcesJar = task<Jar>("sourcesJar") {
            classifier = "sources"
            setDuplicatesStrategy(DuplicatesStrategy.EXCLUDE)
        }

        afterEvaluate {
            var sources: FileTree =
                    if (sourceSets.findByName("main") != null) sourceSets["main"].allSource
                    else {
                        val android = extensions.findByName("android") as? LibraryExtension ?: let {
                            logger.warn("${project.name}: Could not find sources")
                            return@afterEvaluate
                        }
                        android.sourceSets["main"].java.sourceFiles +
                                android.sourceSets["main"].resources.sourceFiles +
                                android.sourceSets["main"].res.sourceFiles +
                                android.sourceSets["main"].assets.sourceFiles +
                                fileTree(android.sourceSets["main"].manifest.srcFile) +
                                android.sourceSets["main"].aidl.sourceFiles +
                                android.sourceSets["main"].jni.sourceFiles +
                                android.sourceSets["main"].jniLibs.sourceFiles +
                                android.sourceSets["main"].renderscript.sourceFiles +
                                android.sourceSets["main"].shaders.sourceFiles
                    }

            val expectedBy = configurations.firstOrNull { it.name == "expectedBy" }
            expectedBy?.dependencies?.forEach {
                val commonProject = (it as DefaultProjectDependency).dependencyProject
                evaluationDependsOn(commonProject.path)
                sources += commonProject.sourceSets["main"].allSource
            }

            sourcesJar.from(sources)
        }
    }

    override fun apply(project: Project) = project.applyPlugin()
}
