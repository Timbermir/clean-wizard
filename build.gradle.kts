import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
}

buildscript {
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
    }
}

allprojects {
    rootProject.project.libs.plugins.apply {
        apply(plugin = kotlin.jvm.get().pluginId)
        apply(plugin = kotlin.serialization.get().pluginId)
        apply(plugin = google.devtools.ksp.get().pluginId)
    }
    kotlinExtension.jvmToolchain(19)
}