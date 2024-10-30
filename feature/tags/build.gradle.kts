plugins {
    alias(libs.plugins.thoughtpad.android.library)
    alias(libs.plugins.thoughtpad.compose.library)
    alias(libs.plugins.thoughtpad.android.koin)
}

android { namespace = "com.gitsoft.thoughtpad.feature.tags" }

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:toga"))
    implementation(project(":core:data"))
}
