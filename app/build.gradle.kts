import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android.gradle)
    alias(libs.plugins.detekt)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()

if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    namespace = "com.gitsoft.thoughtpad"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gitsoft.thoughtpad"
        minSdk = 23
        targetSdk = 34
        versionCode = 3
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        compose = true
    }

    signingConfigs {
        create("release") {
            storeFile = file("../keystore.jks")
            storePassword = System.getenv("RELEASE_KEYSTORE_PASSWORD")
            keyAlias = System.getenv("RELEASE_KEYSTORE_ALIAS")
            keyPassword = System.getenv("RELEASE_KEY_PASSWORD")
        }
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.14" }
}

dependencies {
    implementation(libs.legacy.support.v4)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.fragment.ktx)
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // compose dependencies
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.extended.icons)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // support for datastore
    implementation(libs.androidx.datastore.preferences)

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.room.ktx)

    // ViewModel
    implementation(libs.lifecycle.viewmodel.ktx)
    // LiveData
    implementation(libs.lifecycle.livedata.ktx)
    // Kotlin
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // coroutines
    implementation(libs.kotlinx.coroutines.android)

    // coordinator layout
    implementation(libs.androidx.coordinatorlayout)

    //    implementation ("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
