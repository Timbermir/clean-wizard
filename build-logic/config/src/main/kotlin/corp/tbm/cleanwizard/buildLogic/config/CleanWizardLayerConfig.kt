package corp.tbm.cleanwizard.buildLogic.config

import corp.tbm.cleanwizard.buildLogic.config.dsl.CleanWizardConfigDslMarker
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@CleanWizardConfigDslMarker
@Serializable
sealed class CleanWizardLayerConfig(
    @SerialName("moduleName") open var moduleName: String,
    @SerialName("classSuffix") open var classSuffix: String,
    @SerialName("packageName") open var packageName: String
) {

    @Serializable
    @SerialName("Data")
    data class Data(
        @SerialName("dataModuleName") override var moduleName: String = "data",
        @SerialName("dataClassSuffix") override var classSuffix: String = "DTO",
        @SerialName("dataPackageName") override var packageName: String = "dto",
        var interfaceMapperName: String = "DTOMapper",
        var toDomainMapFunctionName: String = "toDomain",
    ) : CleanWizardLayerConfig(moduleName, classSuffix, packageName)

    @Serializable
    @SerialName("Domain")
    data class Domain(
        @SerialName("domainModuleName") override var moduleName: String = "domain",
        @SerialName("domainClassSuffix") override var classSuffix: String = "Model",
        @SerialName("domainPackageName") override var packageName: String = "model",
        var toDTOMapFunctionName: String = "toDTO",
        var toUIMapFunctionName: String = "toUI",
        var useCaseConfig: CleanWizardUseCaseProcessorConfig = CleanWizardUseCaseProcessorConfig()
    ) : CleanWizardLayerConfig(moduleName, classSuffix, packageName) {

        fun useCase(configuration: CleanWizardUseCaseProcessorConfig.() -> Unit) {
            useCaseConfig.apply(configuration)
        }
    }

    @Serializable
    @SerialName("Presentation")
    data class Presentation(
        @SerialName("presentationModuleName") override var moduleName: String = "presentation",
        @SerialName("presentationClassSuffix") override var classSuffix: String = "UI",
        @SerialName("presentationPackageName") override var packageName: String = "ui",
        var toDomainMapFunctionName: String = "toDomain",
    ) : CleanWizardLayerConfig(moduleName, classSuffix, packageName)
}

@Serializable
data class CleanWizardLayerConfigWrapper(
    var dataConfig: CleanWizardLayerConfig.Data = CleanWizardLayerConfig.Data(),
    var domainConfig: CleanWizardLayerConfig.Domain = CleanWizardLayerConfig.Domain(),
    var presentationConfig: CleanWizardLayerConfig.Presentation = CleanWizardLayerConfig.Presentation()
)