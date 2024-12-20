import config.AndroidSdk
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.thoughtpad.android.application)
    alias(libs.plugins.thoughtpad.compose.application)
    alias(libs.plugins.thoughtpad.android.koin)
    alias(libs.plugins.thoughtpad.google.firebase.application)
    alias(libs.plugins.gms)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.performance)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ksp)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()

if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    namespace = AndroidSdk.namespace

    defaultConfig {
        applicationId = AndroidSdk.applicationId
        versionCode = AndroidSdk.versionCode
        versionName = AndroidSdk.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
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
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    implementation(project(":feature:notelist"))
    implementation(project(":feature:addnote"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:tags"))
    implementation(project(":core:data"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:toga"))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.work)
    implementation(libs.lottie.compose)
    implementation(libs.androidx.glance)
    implementation(libs.androidx.glance.app.widget)
    implementation(libs.androidx.glance.preview)
    androidTestImplementation(libs.androidx.glance.testing)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
