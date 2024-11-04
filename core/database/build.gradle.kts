plugins {
    alias(libs.plugins.thoughtpad.android.library)
    alias(libs.plugins.thoughtpad.android.room)
    alias(libs.plugins.thoughtpad.android.koin)
}

android {
    namespace = "com.gitsoft.thoughtpad.core.database"

    defaultConfig { testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }
    packaging { resources.excludes.add("META-INF/*") }
}

dependencies {
    implementation(project(":core:model"))

    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.test.core)
    androidTestImplementation(libs.coroutines.test)
    androidTestImplementation(libs.truth)
}
