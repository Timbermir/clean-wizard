package corp.tbm.cleanwizard.buildLogic.convention.plugins.codegen

import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.applyPlugin
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.implementation
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

internal class CodegenPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            applyPlugin(libs.plugins.cleanwizard.kotlin)
            applyPlugin(libs.plugins.google.devtools.ksp)

            dependencies {
                implementation(project(":foundation:annotations"))
                implementation(libs.bundles.foundation.codegen)
            }
        }
    }
}
