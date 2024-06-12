package corp.tbm.cleanarchitecturemapper.buildLogic.convention.plugins

import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions.applyPlugin
import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions.implementation
import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions.jvmTarget
import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal class KotlinPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            applyPlugin(libs.plugins.kotlin.jvm)
            applyPlugin(libs.plugins.kotlin.serialization)

            dependencies {
                implementation(libs.bundles.kotlinx)
                implementation(libs.google.gson)
                implementation(libs.squareup.moshi)
                implementation(libs.fasterxml.jackson.databind)
            }

            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set(this@with.jvmTarget)
                }
            }
        }
    }
}