package corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.plugins

import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions.applyPlugin
import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions.implementation
import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions.libs
import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions.pluginConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

internal class CodegenPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            applyPlugin(libs.plugins.cleanarchitecturemapper.kotlin)
            applyPlugin(libs.plugins.google.devtools.ksp)

            dependencies {
                implementation(project(":foundation:annotations"))
                implementation(libs.bundles.foundation.codegen)
            }
        }
    }
}
