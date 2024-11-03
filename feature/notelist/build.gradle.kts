plugins {
    alias(libs.plugins.thoughtpad.android.library)
    alias(libs.plugins.thoughtpad.compose.library)
    alias(libs.plugins.thoughtpad.android.koin)
}

android { namespace = "com.gitsoft.thoughtpad.feature.notelist" }

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:toga"))
    implementation(project(":core:common"))

    implementation(libs.lottie.compose)

    testImplementation(libs.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(libs.test.runner)
    testImplementation(libs.androidx.espresso.core)
    testImplementation(libs.test.core)
}
