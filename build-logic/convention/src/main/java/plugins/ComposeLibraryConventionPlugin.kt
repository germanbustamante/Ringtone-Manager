package plugins

import com.android.build.api.dsl.LibraryExtension
import extensions.get
import extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Baseline for Android library modules that expose Jetpack Compose UI
 * (design system, shared UI, feature modules). Builds on the Android library
 * convention and adds the Compose compiler plugin, the Compose build feature and
 * the common Compose dependencies via the BOM.
 */
class ComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("android.library.convention.plugin")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            configure<LibraryExtension> {
                buildFeatures.compose = true
            }

            dependencies {
                val bom = platform(libs.get("androidx-compose-bom"))
                add("implementation", bom)
                add("implementation", libs.get("androidx-ui"))
                add("implementation", libs.get("androidx-ui-graphics"))
                add("implementation", libs.get("androidx-ui-tooling-preview"))
                add("implementation", libs.get("androidx-material3"))
                add("debugImplementation", libs.get("androidx-ui-tooling"))
            }
        }
    }
}
