plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.germandebustamante.ringtonemanager.data.remote"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":domain"))

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