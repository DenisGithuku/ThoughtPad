package config

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/*
Compose specific configuration
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))

            add("implementation", libs.findLibrary("androidx.activity.compose").get())
            add("implementation", libs.findLibrary("androidx.compose.ui.tooling.preview").get())
            add("implementation", libs.findLibrary("androidx.compose.material3").get())
            add("implementation", libs.findLibrary("androidx.compose.ui").get())
            add("api", libs.findLibrary("androidx.compose.ui.graphics").get())
            add("implementation", libs.findLibrary("androidx.compose.material.extended.icons").get())
            add("implementation", libs.findLibrary("androidx.compose.foundation").get())
            add("implementation", libs.findLibrary("androidx.compose.runtime").get())
            add("implementation", libs.findLibrary("androidx.navigation.compose").get())
            add("implementation", libs.findLibrary("androidx.lifecycle.viewmodel.compose").get())
            add("implementation", libs.findLibrary("androidx.compose.ui.tooling").get())
            add("implementation", libs.findLibrary("androidx.compose.ui.tooling.preview").get())
            add("androidTestImplementation", libs.findLibrary("androidx.compose.ui.test.junit4").get())
            add("debugImplementation", libs.findLibrary("androidx.compose.ui.tooling").get())

            // Adding ComponentActivity to debug manifest
            add("debugImplementation", libs.findLibrary("androidx.compose.ui.test.manifest").get())
        }
    }
}