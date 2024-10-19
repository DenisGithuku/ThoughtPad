import config.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidKoinConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
            }

            dependencies {
                add("implementation", libs.findLibrary("koin.android").get())
                add("ksp", libs.findLibrary("koin.compiler").get())
                add("implementation", libs.findLibrary("koin.annotations").get())
                add("implementation", libs.findLibrary("koin.compose").get())
                add("implementation", libs.findLibrary("koin.core").get())
            }
        }
    }

}