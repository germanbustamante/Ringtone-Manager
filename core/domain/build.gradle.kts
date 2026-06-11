plugins {
    id("kotlin.library.convention.plugin")
    id("detekt.convention.plugin")
    id("unit.test.convention.plugin")
    id("coverage.convention.plugin")
}

dependencies {
    implementation(project(":core:model"))
    //Arrow
    implementation(libs.arrow.core)

    //Kotlin coroutines
    implementation(libs.kotlinx.coroutines.core)
}
