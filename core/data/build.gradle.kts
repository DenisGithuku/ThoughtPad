plugins {
    alias(libs.plugins.thoughtpad.android.library)
    alias(libs.plugins.thoughtpad.android.koin)
}

android { namespace = "com.gitsoft.thoughtpad.core.data" }

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:database"))
}
