import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.vanniktech.maven.publish)
    alias(libs.plugins.cleanwizard.internal.publish)
}

group = projectConfig.versions.group.get()
version = projectConfig.versions.version.get()

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.google.gson)
    implementation(libs.squareup.moshi)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        languageVersion =
            org.jetbrains.kotlin.gradle.dsl.KotlinVersion.values()
                .first { it.version == projectConfig.versions.kotlin.get() }
    }
}