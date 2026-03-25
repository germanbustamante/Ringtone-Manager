plugins {
    `java-test-fixtures`
    id("kotlin.library.convention.plugin")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testFixturesImplementation(libs.kotlinx.coroutines.test)
}
