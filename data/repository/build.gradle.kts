plugins {
    id("android.library.convention.plugin")
    id("detekt.convention.plugin")
    id("unit.test.convention.plugin")
    id("coverage.convention.plugin")
}

android {
    namespace = "com.germandebustamante.ringtonemanager.data.repository"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(Modules.CORE_DOMAIN))
    implementation(project(Modules.CORE_MODEL))
    implementation(project(Modules.DATA_REMOTE))
    implementation(project(Modules.DATA_LOCAL))

    //Koin
    implementation(platform(libs.io.insert.koin.bom))
    implementation(libs.io.insert.koin.core)

    //Arrow
    implementation(libs.arrow.core)

    //Kotlin coroutines
    implementation(libs.kotlinx.coroutines.core)
}
