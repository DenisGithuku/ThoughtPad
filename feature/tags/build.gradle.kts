plugins {
    alias(libs.plugins.thoughtpad.android.library)
    alias(libs.plugins.thoughtpad.compose.library)
    alias(libs.plugins.thoughtpad.android.koin)
}

android {
    namespace = "com.gitsoft.thoughtpad.feature.tags"

    defaultConfig { testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:toga"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))

    testImplementation(libs.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(libs.test.runner)
    testImplementation(libs.androidx.espresso.core)
    testImplementation(libs.test.core)
}
