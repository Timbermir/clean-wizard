package corp.tbm.cleanwizard.gradle.api.config.layerConfigs

import corp.tbm.cleanwizard.gradle.api.config.CleanWizardInterfaceMapperConfig
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardNullResolutionStrategy
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardRoomConfig
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardUseCaseConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class CleanWizardLayerConfig(
    @SerialName("module-name")
    open val moduleName: String,
    @SerialName("class-suffix")
    open val classSuffix: String,
    @SerialName("package-name")
    open val packageName: String
) {

    @Serializable
    @SerialName("Data")
    data class Data(
        override val moduleName: String = "data",
        override val classSuffix: String = "DTO",
        override val packageName: String = "dtos",
        val interfaceMapperConfig: CleanWizardInterfaceMapperConfig = CleanWizardInterfaceMapperConfig(),
        val schemaSuffix: String = "DTOSchema",
        val nullResolutionStrategy: CleanWizardNullResolutionStrategy = CleanWizardNullResolutionStrategy.STUB_GENERATION,
        val toDomainAsTopLevel: Boolean = true,
        val toDomainMapFunctionName: String = "toDomain",
        val roomConfig: CleanWizardRoomConfig = CleanWizardRoomConfig(),
    ) : CleanWizardLayerConfig(moduleName, classSuffix, packageName)

    @Serializable
    @SerialName("Domain")
    data class Domain(
        override val moduleName: String = "domain",
        override val classSuffix: String = "Model",
        override val packageName: String = "models",
        val toDTOMapFunctionName: String = "toDTO",
        val toUIMapFunctionName: String = "toUI",
        val useCaseConfig: CleanWizardUseCaseConfig = CleanWizardUseCaseConfig()
    ) : CleanWizardLayerConfig(moduleName, classSuffix, packageName)

    @Serializable
    @SerialName("Presentation")
    data class Presentation(
        override val moduleName: String = "presentation",
        override val classSuffix: String = "UI",
        override val packageName: String = "ui",
        val shouldGenerate: Boolean = true,
        val toDomainMapFunctionName: String = "toDomain",
    ) : CleanWizardLayerConfig(moduleName, classSuffix, packageName)
}