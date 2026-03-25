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
    }
}