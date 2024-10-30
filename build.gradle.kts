import com.diffplug.gradle.spotless.SpotlessExtension
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude
import org.jetbrains.kotlin.js.inline.clean.removeUnusedImports

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.firebase.performance.gradle.plugin)
        classpath(libs.firebase.crashlytics.gradle.plugin)
        classpath(libs.google.services.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.com.diffplug.spotless) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.firebase.performance) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
}

subprojects {
    apply {
        plugin("com.diffplug.spotless")
        plugin("jacoco")
    }

    configure<SpotlessExtension> {
        val lintVersion = "0.49.0"

        kotlin {
            target("**/*.kt")
            ktlint(lintVersion)
                .editorConfigOverride(
                    mapOf(
                        "ktlint_standard_package-name" to "disabled",
                        "ktlint_standard_filename" to "disabled",
                        "ij_kotlin_allow_trailing_comma" to "false",
                        "ij_kotlin_allow_trailing_comma_on_call_site" to "false",
                    ),
                )
            ktfmt().googleStyle().configure {
                it.setRemoveUnusedImport(true)
            }
            licenseHeaderFile("${project.rootProject.projectDir}/license-header.txt")
            trimTrailingWhitespace()
            indentWithTabs(2)
            indentWithSpaces(4)
            toggleOffOn()
        }

        kotlinGradle {
            target("*.gradle.kts")
            ktlint(lintVersion)
                .editorConfigOverride(
                    mapOf(
                        "ij_kotlin_allow_trailing_comma" to "false",
                        "ij_kotlin_allow_trailing_comma_on_call_site" to "false",
                    )
                )
            ktfmt().googleStyle()
            indentWithTabs(2)
            indentWithSpaces(4)

        }

        format("xml") {
            target("**/*.xml")
            indentWithSpaces()
            trimTrailingWhitespace()
            endWithNewline()
        }

    }

    configurations.all {
        resolutionStrategy {
            eachDependency {
                when (requested.group) {
                    "org.jacoco" -> useVersion("0.8.11")
                }
            }
        }
    }

    tasks.withType<Test> {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            excludes = listOf("jdk.internal.*", "**org.hl7*")
        }
    }
}

detekt {
    toolVersion = "1.23.6"
    config.setFrom(file("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}


tasks.withType<Detekt>().configureEach {
    reports {
        xml.required.set(true)
        html.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
        md.required.set(true)
    }
}
