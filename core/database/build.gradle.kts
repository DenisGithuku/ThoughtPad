plugins {
    alias(libs.plugins.thoughtpad.android.library)
    alias(libs.plugins.thoughtpad.android.room)
    alias(libs.plugins.thoughtpad.android.koin)
}

android { namespace = "com.gitsoft.thoughtpad.core.database" }

dependencies { implementation(project(":core:model")) }