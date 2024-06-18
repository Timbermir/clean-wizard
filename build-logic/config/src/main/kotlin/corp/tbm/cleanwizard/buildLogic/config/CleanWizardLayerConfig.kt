package corp.tbm.cleanwizard.buildLogic.config

import corp.tbm.cleanwizard.buildLogic.config.dsl.CleanWizardConfigDslMarker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@CleanWizardConfigDslMarker
@Serializable
sealed class CleanWizardDataClassGenerationProcessorConfig(
    open var moduleName: String,
    open var classSuffix: String,
    open var packageName: String
) {
    @SerialName("DTO")
    data class DTO(
        override var moduleName: String = "data",
        override var classSuffix: String = "DTO",
        override var packageName: String = "dto",
        var interfaceMapperName: String = "DTOMapper",
        var toDomainMapFunctionName: String = "toDomain",
    ) : CleanWizardDataClassGenerationProcessorConfig(moduleName, classSuffix, packageName)

    @SerialName("Domain")
    data class Domain(
        override var moduleName: String = "domain",
        override var classSuffix: String = "Model",
        override var packageName: String = "model",
        var toDTOMapFunctionName: String = "toDTO",
        var toUIMapFunctionName: String = "toUI",
    ) : CleanWizardDataClassGenerationProcessorConfig(moduleName, classSuffix, packageName)

    @SerialName("Presentation")
    data class Presentation(
        override var moduleName: String = "presentation",
        override var classSuffix: String = "UI",
        override var packageName: String = "ui",
        var toDomainMapFunctionName: String = "toDomain",
    ) : CleanWizardDataClassGenerationProcessorConfig(moduleName, classSuffix, packageName)
}