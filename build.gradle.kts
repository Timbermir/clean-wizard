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