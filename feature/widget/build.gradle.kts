plugins {
    alias(libs.plugins.thoughtpad.android.library)
    alias(libs.plugins.thoughtpad.compose.library)
    alias(libs.plugins.thoughtpad.android.koin)
}

android {
    namespace = "com.gitsoft.thoughtpad.feature.widget"

    defaultConfig { testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:toga"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))

    implementation(libs.androidx.glance)
    implementation(libs.androidx.glance.preview)
    androidTestImplementation(libs.androidx.glance.testing)
}