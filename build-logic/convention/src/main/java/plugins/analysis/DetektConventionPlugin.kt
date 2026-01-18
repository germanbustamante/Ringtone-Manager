package plugins.analysis

import extensions.detekt
import extensions.detektPlugins
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("io.gitlab.arturbosch.detekt")
            }

            dependencies {
                detektPlugins(project, "detekt-formatting")
            }

            detekt {
                source.setFrom(
                    files(
                        "src/main/java",
                        "src/main/kotlin",
                        "src/debug/java",
                        "src/debug/kotlin",
                        "src/release/java",
                        "src/release/kotlin"
                    )
                )
                config.setFrom(files("$rootDir/.config/detekt.yml"))
                buildUponDefaultConfig = true
                ignoreFailures = false
            }

            tasks.withType<Detekt>().configureEach {
                jvmTarget = "17"
                reports {
                    html.required.set(true)
                    xml.required.set(true)
                    txt.required.set(true)
                    sarif.required.set(true)
                    md.required.set(true)
                }
            }
        }
    }
}
