plugins {
    id("android.library.convention.plugin")
    id("detekt.convention.plugin")
    id("unit.test.convention.plugin")
}

android {
    namespace = "com.germandebustamante.ringtonemanager.data.local"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(project(Modules.CORE_MODEL))
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.core)
}
