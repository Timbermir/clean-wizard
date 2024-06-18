package corp.tbm.cleanwizard.buildLogic.convention.plugins

import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.cleanWizardProcessorConfig
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.ksp
import corp.tbm.cleanwizard.buildLogic.convention.processorConfig.CleanWizardProcessorConfig
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

val json = Json {
    classDiscriminator = "type"
    encodeDefaults = true
}

internal class CleanWizardPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            extensions.create("clean-wizard", CleanWizardProcessorConfig::class.java)
            allprojects {
                pluginManager.withPlugin("com.google.devtools.ksp") {
                    afterEvaluate {
                        println(this.path)
                        ksp {
                            cleanWizardProcessorConfig.apply {
                                val diFrameworkTempFile = File.createTempFile("di-framework", ".json")
                                val useCaseFunctionTypeTempFile = File.createTempFile("use-case-function-type", ".json")
                                useCaseFunctionTypeTempFile.writeText(json.encodeToString(useCaseConfig.useCaseProcessorFunctionType))
                                diFrameworkTempFile.writeText(json.encodeToString(diFramework))
                                arg("DATA_CLASS_GENERATION_PATTERN", dataClassGenerationPattern.name)
                                arg("JSON_SERIALIZER", jsonSerializer.serializer)
                                arg("DEPENDENCY_INJECTION_FRAMEWORK", dependencyInjectionFramework.name)
                                arg(
                                    "DI_FRAMEWORK",
                                    diFrameworkTempFile.absolutePath
                                )
                                arg("DATA_MODULE_NAME", dtoConfig.moduleName)
                                arg("DTO_CLASS_SUFFIX", dtoConfig.classSuffix)
                                arg("DTO_CLASS_PACKAGE_NAME", dtoConfig.packageName)
                                arg("DTO_INTERFACE_MAPPER_NAME", dtoConfig.interfaceMapperName)
                                arg("DTO_TO_DOMAIN_MAP_FUNCTION_NAME", dtoConfig.toDomainMapFunctionName)
                                arg("DOMAIN_MODULE_NAME", domainConfig.moduleName)
                                arg("DOMAIN_CLASS_SUFFIX", domainConfig.classSuffix)
                                arg("DOMAIN_CLASS_PACKAGE_NAME", domainConfig.packageName)
                                arg("DOMAIN_TO_DTO_MAP_FUNCTION_NAME", domainConfig.toDTOMapFunctionName)
                                arg("DOMAIN_TO_UI_MAP_FUNCTION_NAME", domainConfig.toUIMapFunctionName)
                                arg("PRESENTATION_MODULE_NAME", presentationConfig.moduleName)
                                arg("UI_CLASS_SUFFIX", presentationConfig.classSuffix)
                                arg("UI_CLASS_PACKAGE_NAME", presentationConfig.packageName)
                                arg("UI_TO_DOMAIN_MAP_FUNCTION_NAME", presentationConfig.toDomainMapFunctionName)
                                arg("USE_CASE_SUFFIX", useCaseConfig.suffix)
                                arg("USE_CASE_PACKAGE_NAME", useCaseConfig.packageName)
                                arg(
                                    "USE_CASE_FUNCTION_TYPE",
                                    useCaseFunctionTypeTempFile.absolutePath
                                )
                                arg("USE_CASE_CREATE_WRAPPER", useCaseConfig.createWrapper.toString())
                            }
                        }
                    }
                }
            }
        }
    }
}