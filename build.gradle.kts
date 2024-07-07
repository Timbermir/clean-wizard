import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDataClassGenerationPattern
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardUseCaseFunctionType

plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.cleanwizard.core)
    alias(libs.plugins.vanniktech.maven.publish) apply false
}

buildscript {
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
    }
}

`clean-wizard` {

    `json-serializer` {
        `kotlinx-serialization` {
            delimiter = "_"
        }
    }

    dataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER

    `dependency-injection` {
        kodein {
            useSimpleFunctions = true
            binding = CleanWizardDependencyInjectionFramework.Kodein.KodeinBinding.Multiton()
        }
    }

    data {
        classSuffix = "DTO"
        packageName = "dtos"
        interfaceMapperName = "DtoMapper"
        toDomainMapFunctionName = "toModel"
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