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
    }
}