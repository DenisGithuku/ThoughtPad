plugins {
    alias(libs.plugins.thoughtpad.android.library)
    alias(libs.plugins.thoughtpad.android.koin)
}

android { namespace = "com.gitsoft.thoughtpad.core.datastore" }

dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.datastore.preferences)
}
