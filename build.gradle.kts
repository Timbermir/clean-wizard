import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDataClassGenerationPattern
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardUseCaseFunctionType
import corp.tbm.cleanwizard.buildLogic.config.KodeinBinding

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

    `json-serializer` {
        `kotlinx-serialization` {
            delimiter = "_"
        }
    }

    dataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER

    `dependency-injection` {
        kodein {
            useSimpleFunctions = true
            binding = KodeinBinding.Multiton()
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

tasks.register("deleteOrphanedDirs", Delete::class) {
    delete(
        "build-logic/config/build",
        "build-logic/convention/build",
        "build-logic/.gradle",
        "build-logic/build",
    )
}
tasks.prepareKotlinBuildScriptModel { dependsOn("deleteOrphanedDirs") }