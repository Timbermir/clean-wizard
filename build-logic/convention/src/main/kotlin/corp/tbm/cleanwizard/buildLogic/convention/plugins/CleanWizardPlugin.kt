package corp.tbm.cleanwizard.buildLogic.convention.plugins

import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.cleanWizardExtension
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.createTempFileWithEncodedString
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.json
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.ksp
import corp.tbm.cleanwizard.buildLogic.convention.plugins.extensions.CleanWizardExtensionImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class CleanWizardPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            extensions.create("clean-wizard", CleanWizardExtensionImplementation::class.java)
            allprojects {
                pluginManager.withPlugin("com.google.devtools.ksp") {
                    afterEvaluate {
                        ksp {
                            cleanWizardExtension.apply {
                                arg(
                                    "DATA_CLASS_GENERATION_PATTERN",
                                    dataClassGenerationPattern.name
                                )
                                arg(
                                    "JSON_SERIALIZER",
                                    json.createTempFileWithEncodedString(jsonSerializer).absolutePath
                                )
                                arg(
                                    "DEPENDENCY_INJECTION_FRAMEWORK",
                                    json.createTempFileWithEncodedString(dependencyInjectionFramework).absolutePath
                                )
                                arg(
                                    "LAYER_CONFIGS",
                                    json.createTempFileWithEncodedString(layerConfigs).absolutePath
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}