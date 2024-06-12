package corp.tbm.cleanarchitecturemapper.buildLogic.convention.plugins

import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.CleanWizardProcessorConfig
import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions.ksp
import org.gradle.api.Plugin
import org.gradle.api.Project

class CleanWizardPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.create("clean-wizard", CleanWizardProcessorConfig::class.java)

            allprojects {
                pluginManager.withPlugin("com.google.devtools.ksp") {
                    afterEvaluate {
                        ksp {
                            rootProject.extensions.findByType(CleanWizardProcessorConfig::class.java)
                                ?.let { extension ->
                                    arg("DATA_MODULE_NAME", extension.dataModuleName)
                                    arg("DOMAIN_MODULE_NAME", extension.domainModuleName)
                                    arg("PRESENTATION_MODULE_NAME", extension.presentationModuleName)
                                    arg("DTO_CLASS_SUFFIX", extension.dtoClassSuffix)
                                    arg("DTO_CLASS_PACKAGE_NAME", extension.dtoClassPackageName)
                                    arg("DTO_INTERFACE_MAPPER_NAME", extension.dtoInterfaceMapperName)
                                    arg("DTO_TO_DOMAIN_MAP_FUNCTION_NAME", extension.dtoToDomainMapFunctionName)
                                    arg("DOMAIN_TO_DTO_MAP_FUNCTION_NAME", extension.domainToDtoMapFunctionName)
                                    arg("DOMAIN_CLASS_SUFFIX", extension.domainClassSuffix)
                                    arg("DOMAIN_CLASS_PACKAGE_NAME", extension.domainClassPackageName)
                                    arg("UI_CLASS_SUFFIX", extension.uiClassSuffix)
                                    arg("UI_CLASS_PACKAGE_NAME", extension.uiClassPackageName)
                                    arg("DOMAIN_TO_UI_MAP_FUNCTION_NAME", extension.domainToUiMapFunctionName)
                                    arg("UI_TO_DOMAIN_MAP_FUNCTION_NAME", extension.uiToDomainMapFunctionName)
                                    arg("DEFAULT_JSON_SERIALIZER", extension.defaultJsonSerializer.serializer)
                                }
                        }
                    }
                }
            }
        }
    }
}