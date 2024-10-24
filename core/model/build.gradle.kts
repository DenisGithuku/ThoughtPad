plugins {
    alias(libs.plugins.thoughtpad.android.library)
    alias(libs.plugins.thoughtpad.android.room)
}

android { namespace = "com.gitsoft.thoughtpad.core.model" }

dependencies {}

val isCiBuild = System.getenv("CI")?.toBoolean() ?: false

// Disables KSP generating room schema when running on CI
ksp {
    if (isCiBuild) {
        arg("ksp.schema", "false")
    }
}
