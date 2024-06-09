package corp.tbm.cleanarchitecturemapper.buildLogic.convention.plugins.codegen

import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions.applyPlugin
import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions.implementation
import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class VisitorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            applyPlugin(libs.plugins.cleanarchitecturemapper.codegen.foundation)

            dependencies {
                implementation(project(":foundation:codegen:universal"))
                implementation(project(":foundation:codegen:kotlinpoet"))
                implementation(project(":foundation:codegen:ksp"))
            }
        }
    }
}