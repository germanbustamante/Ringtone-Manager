package plugins.analysis

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Applies Kover to a module so its code is instrumented for coverage. The merged
 * report and the verification gate are configured in the root build, which
 * aggregates every module that applies this plugin.
 */
class CoverageConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project.pluginManager) {
            apply("org.jetbrains.kotlinx.kover")
        }
    }
}
