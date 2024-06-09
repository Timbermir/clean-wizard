import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.CleanWizardJsonSerializer

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.cleanwizard)
}

buildscript {
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
    }
}

`clean-wizard` {
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
    defaultJsonSerializer = CleanWizardJsonSerializer.JACKSON
}