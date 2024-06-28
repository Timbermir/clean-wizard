package corp.tbm.cleanwizard.buildLogic.config

import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@CleanWizardConfigDsl
@Serializable
sealed class CleanWizardLayerConfig(
    var moduleName: String,
    var classSuffix: String,
    var packageName: String
) {

    @Serializable
    @SerialName("Data")
    data class Data(
        var schemaSuffix: String = "DTOSchema",
        var interfaceMapperName: String = "DTOMapper",
        var toDomainMapFunctionName: String = "toDomain",
        val roomConfig: CleanWizardRoomConfig = CleanWizardRoomConfig(),
    ) : CleanWizardLayerConfig("data", "DTO", "dto") {

        fun room(block: CleanWizardRoomConfig.() -> Unit) {
            roomConfig.apply(block)
        }
    }

    @Serializable
    @SerialName("Domain")
    data class Domain(
        var toDTOMapFunctionName: String = "toDTO",
        var toUIMapFunctionName: String = "toUI",
        val useCaseConfig: CleanWizardUseCaseConfig = CleanWizardUseCaseConfig()
    ) : CleanWizardLayerConfig("domain", "Model", "model") {

        fun useCase(block: CleanWizardUseCaseConfig.() -> Unit) {
            useCaseConfig.apply(block)
        }
    }

    @Serializable
    @SerialName("Presentation")
    data class Presentation(
        var toDomainMapFunctionName: String = "toDomain",
    ) : CleanWizardLayerConfig("presentation", "UI", "ui")
}