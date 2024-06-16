package corp.tbm.cleanwizard.buildLogic.convention.processorConfig

import corp.tbm.cleanwizard.buildLogic.convention.foundation.dsl.CleanWizardProcessorConfigDslMarker

@CleanWizardProcessorConfigDslMarker
sealed class CleanWizardDataClassGenerationProcessorConfig(
    open var moduleName: String,
    open var suffix: String,
    open var packageName: String
) {
    data class DTO(
        override var moduleName: String = "data",
        override var suffix: String = "DTO",
        override var packageName: String = "dto",
        var interfaceMapperName: String = "DTOMapper",
        var toDomainMapFunctionName: String = "toDomain",
    ) : CleanWizardDataClassGenerationProcessorConfig(moduleName, suffix, packageName)

    data class Domain(
        override var moduleName: String = "domain",
        override var suffix: String = "Model",
        override var packageName: String = "model",
        var toDTOMapFunctionName: String = "toDTO",
        var toUIMapFunctionName: String = "toUI",
    ) : CleanWizardDataClassGenerationProcessorConfig(moduleName, suffix, packageName)

    data class Presentation(
        override var moduleName: String = "presentation",
        override var suffix: String = "UI",
        override var packageName: String = "ui",
        var toDomainMapFunctionName: String = "toDomain",
    ) : CleanWizardDataClassGenerationProcessorConfig(moduleName, suffix, packageName)
}