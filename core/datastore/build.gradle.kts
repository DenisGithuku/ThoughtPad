plugins {
    alias(libs.plugins.thoughtpad.android.library)
    alias(libs.plugins.thoughtpad.android.koin)
}

android {
    namespace = "com.gitsoft.thoughtpad.core.datastore"

    defaultConfig { testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.datastore.preferences)
    testImplementation(libs.mockito.core)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(libs.test.runner)
    testImplementation(libs.androidx.espresso.core)
    testImplementation(libs.test.core)
    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.test.core)
}
