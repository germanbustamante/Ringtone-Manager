package extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

internal fun DependencyHandler.detektPlugins(project: Project, dependency: String) {
    add("detektPlugins", project.libs.get(dependency))
}

internal fun DependencyHandler.implementation(project: Project, dependency: String) {
    add("implementation", project.libs.get(dependency))
}

internal fun DependencyHandler.ksp(project: Project, dependency: String) {
    add("ksp", project.libs.get(dependency))
}

internal fun DependencyHandler.testImplementation(project: Project, dependency: String) {
    add("testImplementation", project.libs.get(dependency))
}

internal fun DependencyHandler.testImplementationPlatform(project: Project, dependency: String) {
    add("testImplementation", platform(project.libs.get(dependency)))
}

internal fun DependencyHandler.testImplementationFixtures(module: String) {
    add("testImplementation", testFixtures(project(module)))
}

internal fun DependencyHandler.testRuntimeOnly(project: Project, dependency: String) {
    add("testRuntimeOnly", project.libs.get(dependency))
}