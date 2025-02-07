plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    //Arrow
    implementation(libs.arrow.core)

    //Kotlin coroutines
    implementation(libs.kotlinx.coroutines.core)
}