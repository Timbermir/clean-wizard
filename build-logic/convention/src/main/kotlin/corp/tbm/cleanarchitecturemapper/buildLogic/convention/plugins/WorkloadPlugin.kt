package corp.tbm.cleanarchitecturemapper.buildLogic.convention.plugins

import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions.applyPlugin
import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions.implementation
import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions.ksp
import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class WorkloadPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            applyPlugin(libs.plugins.cleanarchitecturemapper.codegen.foundation)

            dependencies {
                implementation(project(":foundation:codegen:universal"))
                implementation(project(":processor"))
                ksp(project(":processor"))
            }

            configure<KotlinJvmProjectExtension> {

                sourceSets.getByName("main") {
                    kotlin.srcDir("build/generated/ksp/main/kotlin")
                }

                sourceSets.getByName("test") {
                    kotlin.srcDir("build/generated/ksp/test/kotlin")
                }
            }
        }
    }
}