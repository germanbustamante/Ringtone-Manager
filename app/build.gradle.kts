import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

// Release signing credentials are resolved from a gitignored keystore.properties
// (local) or environment variables (CI). When neither is present the release
// build falls back to the debug signing config so local `assembleRelease` works.
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) keystorePropertiesFile.inputStream().use(::load)
}

fun signingValue(propertyKey: String, envKey: String): String? =
    keystoreProperties.getProperty(propertyKey) ?: System.getenv(envKey)

val hasReleaseKeystore: Boolean = signingValue("storeFile", "KEYSTORE_FILE") != null

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.services)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.compose)
    id("detekt.convention.plugin")
    id("unit.test.convention.plugin")
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "com.germandebustamante.ringtonemanager"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.germandebustamante.ringtonemanager"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        androidResources.localeFilters.addAll(listOf("en", "es"))
    }

    signingConfigs {
        create("release") {
            if (hasReleaseKeystore) {
                storeFile = file(signingValue("storeFile", "KEYSTORE_FILE")!!)
                storePassword = signingValue("storePassword", "KEYSTORE_PASSWORD")
                keyAlias = signingValue("keyAlias", "KEY_ALIAS")
                keyPassword = signingValue("keyPassword", "KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName(if (hasReleaseKeystore) "release" else "debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }
    buildFeatures {
        compose = true
    }

    lint {
        baseline = file("lint-baseline.xml")
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
            all { test ->
                test.filter {
                    excludeTestsMatching("*.screenshot.*")
                }
            }
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //Modules
    implementation(project(Modules.CORE_DOMAIN))
    implementation(project(Modules.CORE_MODEL))
    implementation(project(Modules.BRIDGE_DI))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.appcompat)

    //Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    //Navigation Compose
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlin.serialization.json)

    //Compose
    androidTestImplementation(platform(libs.androidx.compose.bom))

    //Testing
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(platform(libs.io.insert.koin.bom))
    testImplementation(libs.io.insert.koin.test)
    testRuntimeOnly(libs.junit.vintage.engine)

    //AndroidX Media3
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)

    //Koin
    implementation(platform(libs.io.insert.koin.bom))
    implementation(libs.io.insert.koin.core)
    implementation(libs.io.insert.koin.compose)
    implementation(libs.io.insert.koin.android)

    //Arrow
    implementation(libs.arrow.core)

    //Firebase
    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.analytics)
    implementation(libs.google.firebase.firestore)
    implementation(libs.google.firebase.firestore.ktx)
    implementation(libs.google.firebase.storage)
    implementation(libs.firebase.appcheck.playintegrity)
    implementation(libs.firebase.appcheck.debug)
    implementation(libs.firebase.auth)

    //Google Credential Manager
    implementation(libs.androidx.credentials)
    implementation(libs.googleid)
    implementation(libs.androidx.credentials.play.services.auth)

    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.adaptive.navigation3)
    implementation(libs.kotlinx.serialization.core)
}

