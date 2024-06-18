package corp.tbm.cleanwizard.buildLogic.convention.plugins

//import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.cleanWizardProcessorConfig
//import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.ksp
//import corp.tbm.cleanwizard.buildLogic.convention.processorConfig.CleanWizardProcessorConfig
//import kotlinx.serialization.encodeToString
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.cleanWizardExtension
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.createTempFileWithEncodedString
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.ksp
import corp.tbm.cleanwizard.buildLogic.convention.plugins.extensions.CleanWizardExtension
import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project

val json = Json {
    classDiscriminator = "type"
    encodeDefaults = true
}

internal class CleanWizardPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            extensions.create("clean-wizard", CleanWizardExtension::class.java)
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
                                    json.createTempFileWithEncodedString(cleanWizardLayerConfigWrapper).absolutePath
                                )
                                arg(
                                    "DATA_CONFIG",
                                    json.createTempFileWithEncodedString(dataConfig).absolutePath
                                )
                                arg(
                                    "DOMAIN_CONFIG",
                                    json.createTempFileWithEncodedString(domainConfig).absolutePath
                                )
                                arg(
                                    "PRESENTATION_CONFIG",
                                    json.createTempFileWithEncodedString(presentationConfig).absolutePath
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}