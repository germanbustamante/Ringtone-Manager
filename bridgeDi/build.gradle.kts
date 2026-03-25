plugins {
    id("android.library.convention.plugin")
    id("detekt.convention.plugin")
}

android {
    namespace = "com.germandebustamante.ringtonemanager.bridgedi"
    defaultConfig {
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
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":data:repository"))
    implementation(project(":data:remote"))
    implementation(project(":analytics"))

    //Koin
    implementation(platform(libs.io.insert.koin.bom))
    implementation(libs.io.insert.koin.core)

    //Firebase
    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.analytics)
    implementation(libs.google.firebase.firestore.ktx)
    implementation(libs.google.firebase.storage)
    implementation(libs.firebase.auth)
}
