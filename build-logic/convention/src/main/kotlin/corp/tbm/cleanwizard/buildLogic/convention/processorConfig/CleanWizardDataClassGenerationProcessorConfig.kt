package corp.tbm.cleanwizard.buildLogic.convention.processorConfig

import corp.tbm.cleanwizard.buildLogic.convention.foundation.dsl.CleanWizardProcessorConfigDslMarker

@CleanWizardProcessorConfigDslMarker
sealed class CleanWizardDataClassGenerationProcessorConfig(
    open var moduleName: String,
    open var classSuffix: String,
    open var packageName: String
) {
    data class DTO(
        override var moduleName: String = "data",
        override var classSuffix: String = "DTO",
        override var packageName: String = "dto",
        var interfaceMapperName: String = "DTOMapper",
        var toDomainMapFunctionName: String = "toDomain",
    ) : CleanWizardDataClassGenerationProcessorConfig(moduleName, classSuffix, packageName)

    data class Domain(
        override var moduleName: String = "domain",
        override var classSuffix: String = "Model",
        override var packageName: String = "model",
        var toDTOMapFunctionName: String = "toDTO",
        var toUIMapFunctionName: String = "toUI",
    ) : CleanWizardDataClassGenerationProcessorConfig(moduleName, classSuffix, packageName)

    data class Presentation(
        override var moduleName: String = "presentation",
        override var classSuffix: String = "UI",
        override var packageName: String = "ui",
        var toDomainMapFunctionName: String = "toDomain",
    ) : CleanWizardDataClassGenerationProcessorConfig(moduleName, classSuffix, packageName)
}