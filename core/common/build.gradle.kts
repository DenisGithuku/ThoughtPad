plugins { alias(libs.plugins.thoughtpad.android.library) }

android { namespace = "com.gitsoft.thoughtpad.core.common" }

dependencies {
    implementation(libs.coroutines.test)
    implementation(libs.test.runner)
    implementation(libs.test.rules)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(libs.test.runner)
    testImplementation(libs.androidx.espresso.core)
    testImplementation(libs.test.core)
}
