import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.gitsoft.thoughtpad.build-logic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_1
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.room.gradle.plugin)
    compileOnly(libs.firebase.crashlytics.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("android-application") {
            id = "thoughtpad.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("android-library") {
            id = "thoughtpad.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("android-compose-application") {
            id = "thoughtpad.compose.application"
            implementationClass = "AndroidComposeApplicationConventionPlugin"
        }
        register("android-compose-library") {
            id = "thoughtpad.compose.library"
            implementationClass = "AndroidComposeLibraryConventionPlugin"
        }
        register("android-room") {
            id = "thoughtpad.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("firebase-crashlytics") {
            id = "thoughtpad.google.firebase.application"
            implementationClass = "AndroidFirebaseCrashlyticsConventionPlugin"
        }
        register("android-testing") {
            id = "thoughtpad.android.testing"
            implementationClass = "AndroidTestConventionPlugin"
        }
    }
}