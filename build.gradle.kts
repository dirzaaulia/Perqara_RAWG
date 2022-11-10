import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application") version Version.toolsBuildGradle apply false
    id("org.jetbrains.kotlin.android") version Version.kotlin apply false
    id("org.jetbrains.kotlin.kapt") version Version.kotlin apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version Version.kotlin apply false
    id("com.google.dagger.hilt.android") version Version.hiltGradle apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "11" }