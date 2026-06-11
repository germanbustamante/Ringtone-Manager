plugins {
    id("android.library.convention.plugin")
    id("detekt.convention.plugin")
    id("unit.test.convention.plugin")
    id("coverage.convention.plugin")
}

android {
    namespace = "com.germandebustamante.ringtonemanager.data.remote"
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

    //Arrow
    implementation(libs.arrow.core)

    //Firebase
    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.firestore.ktx)
    implementation(libs.google.firebase.storage)
    implementation(libs.firebase.auth)

    //Kotlin coroutines
    implementation(libs.kotlinx.coroutines.core)
}
