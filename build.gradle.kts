import corp.tbm.cleanwizard.gradle.api.config.CleanWizardDataClassGenerationPattern
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardUseCaseFunctionType
import kotlinx.serialization.json.JsonNamingStrategy

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.cleanwizard.core)
    alias(libs.plugins.cleanwizard.multimodule) apply false
    alias(libs.plugins.vanniktech.maven.publish) apply false
}

buildscript {
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
    }
}

`clean-wizard` {

    jsonSerializer {
        kotlinXSerialization {
            json {
                encodeDefaults = true
                prettyPrint = true
                explicitNulls = false
                namingStrategy = JsonNamingStrategy.KebabCase
            }
        }
    }

    dataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER

    dependencyInjection {
        kodein {
            useSimpleFunctions = true
            binding = CleanWizardDependencyInjectionFramework.Kodein.KodeinBinding.Multiton()
        }
    }

    data {
        classSuffix = "DTO"
        packageName = "dtos"
        toDomainMapFunctionName = "toModel"
        interfaceMapper {
            className = "DTOMapper"
            pathToModuleToGenerateInterfaceMapper = projects.cleanWizard.dependencyProject.name
        }
    }

    domain {
        classSuffix = "Domain"
        packageName = "models"
        toDTOMapFunctionName = "fromDomain"
        toUIMapFunctionName = "toUI"
        useCase {
            packageName = "useCase"
            useCaseFunctionType = CleanWizardUseCaseFunctionType.CustomFunctionName("execute")
            classSuffix = "UseCase"
        }
    }

    presentation {
        moduleName = "ui"
        classSuffix = "Ui"
        packageName = "uis"
        toDomainMapFunctionName = "fromUI"
    }
}

tasks.register("deleteOrphanedDirs", Delete::class) {
    delete(
        "build-logic/config/build",
        "build-logic/convention/build",
        "build-logic/.gradle",
        "build-logic/build",
    )
}
tasks.prepareKotlinBuildScriptModel { dependsOn("deleteOrphanedDirs") }