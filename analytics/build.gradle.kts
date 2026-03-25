plugins {
    id("android.library.convention.plugin")
    id("detekt.convention.plugin")
    id("unit.test.convention.plugin")
}

android {
    namespace = "com.germandebustamante.analytics"
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
    implementation(project(Modules.CORE_MODEL))
    implementation(project(Modules.CORE_DOMAIN))

    //Firebase
    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.analytics)
}
