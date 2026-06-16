plugins {
    id("android.library.convention.plugin")
    id("detekt.convention.plugin")
    id("unit.test.convention.plugin")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.germandebustamante.ringtonemanager.data.local"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(project(Modules.CORE_MODEL))

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.androidx.room.testing)
}
