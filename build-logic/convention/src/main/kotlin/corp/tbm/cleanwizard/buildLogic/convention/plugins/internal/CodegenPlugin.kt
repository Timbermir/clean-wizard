package corp.tbm.cleanwizard.buildLogic.convention.plugins.internal

import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.alias
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.implementation
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

internal class CodegenPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            alias(libs.plugins.cleanwizard.internal.kotlin)

            dependencies {
                implementation(project(":foundation:annotations"))
                implementation("build-logic:config")
                implementation(libs.bundles.foundation.codegen)
            }
        }
    }
}