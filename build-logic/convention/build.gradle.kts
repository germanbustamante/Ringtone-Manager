plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.gradlePlugin.kotlin)
    implementation(libs.gradlePlugin.android)
    implementation(libs.gradlePlugin.detekt)
    implementation(libs.gradlePlugin.kover)
    implementation(libs.gradlePlugin.ksp)
}

gradlePlugin {
    plugins {
        register("detektConventionPlugin") {
            id = "detekt.convention.plugin"
            implementationClass = "plugins.analysis.DetektConventionPlugin"
        }
        register("unitTestConventionPlugin") {
            id = "unit.test.convention.plugin"
            implementationClass = "plugins.analysis.UnitTestConventionPlugin"
        }
        register("androidLibraryConventionPlugin") {
            id = "android.library.convention.plugin"
            implementationClass = "plugins.AndroidLibraryConventionPlugin"
        }
        register("kotlinLibraryConventionPlugin") {
            id = "kotlin.library.convention.plugin"
            implementationClass = "plugins.KotlinLibraryConventionPlugin"
        }
        register("coverageConventionPlugin") {
            id = "coverage.convention.plugin"
            implementationClass = "plugins.analysis.CoverageConventionPlugin"
        }
        register("composeLibraryConventionPlugin") {
            id = "compose.library.convention.plugin"
            implementationClass = "plugins.ComposeLibraryConventionPlugin"
        }
    }
}