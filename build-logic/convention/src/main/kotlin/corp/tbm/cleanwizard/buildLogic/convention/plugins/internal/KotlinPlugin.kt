package corp.tbm.cleanwizard.buildLogic.convention.plugins.internal

import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal class KotlinPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            alias(libs.plugins.kotlin.jvm)
            alias(libs.plugins.kotlin.serialization)

            dependencies {
                implementation(libs.bundles.kotlinx)
                implementation(libs.google.gson)
                implementation(libs.squareup.moshi)
                implementation(libs.squareup.moshi.kotlin)
            }

            sourceSets {
                getByName("main") {
                    kotlin.srcDir("build/generated/ksp/main/kotlin")
                }

                getByName("test") {
                    kotlin.srcDir("build/generated/ksp/test/kotlin")
                }
            }

            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set(this@with.jvmTarget)
                }
            }
        }
    }
}