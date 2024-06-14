package corp.tbm.cleanwizard.buildLogic.convention.plugins

import corp.tbm.cleanwizard.buildLogic.convention.foundation.CleanWizardProcessorConfig
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.cleanWizardProcessorConfig
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.ksp
import org.gradle.api.Plugin
import org.gradle.api.Project

class CleanWizardPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.create("clean-wizard", CleanWizardProcessorConfig::class.java)

            allprojects {
                pluginManager.withPlugin("com.google.devtools.ksp") {
                    afterEvaluate {
                        scanForMissingDependencies()
                        ksp {
                            cleanWizardProcessorConfig.apply {
                                arg("DATA_CLASS_GENERATION_PATTERN", dataClassGenerationPattern.name)
                                arg("JSON_SERIALIZER", jsonSerializer.serializer)
                                arg("DEPENDENCY_INJECTION_FRAMEWORK", dependencyInjectionFramework.name)
                                arg("DATA_MODULE_NAME", dataModuleName)
                                arg("DOMAIN_MODULE_NAME", domainModuleName)
                                arg("PRESENTATION_MODULE_NAME", presentationModuleName)
                                arg("DTO_CLASS_SUFFIX", dtoClassSuffix)
                                arg("DTO_CLASS_PACKAGE_NAME", dtoClassPackageName)
                                arg("DTO_INTERFACE_MAPPER_NAME", dtoInterfaceMapperName)
                                arg("DTO_TO_DOMAIN_MAP_FUNCTION_NAME", dtoToDomainMapFunctionName)
                                arg("DOMAIN_TO_DTO_MAP_FUNCTION_NAME", domainToDtoMapFunctionName)
                                arg("DOMAIN_CLASS_SUFFIX", domainClassSuffix)
                                arg("DOMAIN_CLASS_PACKAGE_NAME", domainClassPackageName)
                                arg("UI_CLASS_SUFFIX", uiClassSuffix)
                                arg("UI_CLASS_PACKAGE_NAME", uiClassPackageName)
                                arg("DOMAIN_TO_UI_MAP_FUNCTION_NAME", domainToUiMapFunctionName)
                                arg("UI_TO_DOMAIN_MAP_FUNCTION_NAME", uiToDomainMapFunctionName)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun Project.scanForMissingDependencies() {
        val dependencies = project.configurations.flatMap { configuration ->
            configuration.dependencies.map { dependency -> "${dependency.group}:${dependency.name}" }
        }.toSet()

        if (!dependencies.contains(cleanWizardProcessorConfig.jsonSerializer.dependency)) {
            error("[${cleanWizardProcessorConfig.jsonSerializer.serializer}] serializer option is applied at the root, but no [${cleanWizardProcessorConfig.jsonSerializer.dependency}] dependency was found.")
        }
    }
}