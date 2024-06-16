package corp.tbm.cleanwizard.buildLogic.convention.plugins.codegen

import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.alias
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.implementation
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class VisitorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            alias(libs.plugins.cleanwizard.codegen.foundation)

            dependencies {
                implementation(project(":foundation:codegen:universal"))
                implementation(project(":foundation:codegen:kotlinpoet"))
                implementation(project(":foundation:codegen:ksp"))
            }
        }
    }
}