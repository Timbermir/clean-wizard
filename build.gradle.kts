import corp.tbm.cleanwizard.buildLogic.convention.foundation.CleanWizardDataClassGenerationPattern
import corp.tbm.cleanwizard.buildLogic.convention.foundation.CleanWizardJsonSerializer

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
    jsonSerializer = CleanWizardJsonSerializer.JACKSON
    dataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER
    dataModuleName = "dataLayer"
    dtoClassSuffix = "Dto"
    dtoClassPackageName = "dtos"
    dtoInterfaceMapperName = "DtoMapper"
    dtoToDomainMapFunctionName = "toModel"
    domainToDtoMapFunctionName = "fromDomain"
    domainClassSuffix = "Domain"
    domainClassPackageName = "models"
    uiClassSuffix = "Ui"
    uiClassPackageName = "uis"
    domainToUiMapFunctionName = "toUI"
    uiToDomainMapFunctionName = "fromUI"
}