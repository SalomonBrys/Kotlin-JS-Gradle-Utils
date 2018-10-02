import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.*

@Suppress("unused")
fun Project.mavenPublish() {

    apply {
        plugin("maven-publish")
        plugin("com.jfrog.bintray")
    }

    val sourcesJar = task<Jar>("sourcesJar") {
        classifier = "sources"
        setDuplicatesStrategy(DuplicatesStrategy.EXCLUDE)
    }

    afterEvaluate {
        sourcesJar.from(convention.getPluginByName<JavaPluginConvention>("java").sourceSets["main"].allSource)
    }

    extensions.configure<PublishingExtension>("publishing") {
        (publications) {
            "KMP"(MavenPublication::class) {
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
                repo = "KMP-Gradle-Utils"
                name = project.name
                setLicenses("MIT")
                websiteUrl = "https://github.com/SalomonBrys/KMP-Gradle-Utils"
                issueTrackerUrl = "https://github.com/SalomonBrys/KMP-Gradle-Utils/issues"
                vcsUrl = "https://github.com/SalomonBrys/KMP-Gradle-Utils.git"

                setPublications("KMP")
            })

        }
    }
}
