import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDataClassGenerationPattern
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardJsonSerializer
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardUseCaseProcessorFunctionType

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.cleanwizard.core)
    idea
}

buildscript {
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
    }
}

`clean-wizard` {

    jsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization
    dependencyInjectionFramework = CleanWizardDependencyInjectionFramework.Koin()
    dataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER

    data {
        classSuffix = "Dto"
        packageName = "dtos"
        interfaceMapperName = "DtoMapper"
        toDomainMapFunctionName = "toModel"
    }

    presentation {
        classSuffix = "Ui"
        packageName = "uis"
        toDomainMapFunctionName = "fromUI"
    }

    domain {
        classSuffix = "Domain"
        packageName = "models"
        toDTOMapFunctionName = "fromDomain"
        toUIMapFunctionName = "toUI"
        useCase {
            packageName = "usecase"
            useCaseProcessorFunctionType = CleanWizardUseCaseProcessorFunctionType.CustomFunctionName("execute")
            classSuffix = "useCase"
        }
    }
}