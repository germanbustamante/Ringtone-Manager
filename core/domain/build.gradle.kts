plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("unit.test.convention.plugin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":core:model"))
    //Arrow
    implementation(libs.arrow.core)

    //Kotlin coroutines
    implementation(libs.kotlinx.coroutines.core)
}