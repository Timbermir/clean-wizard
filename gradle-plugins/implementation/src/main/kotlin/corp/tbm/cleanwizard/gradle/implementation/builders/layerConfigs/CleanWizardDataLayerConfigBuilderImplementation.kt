package corp.tbm.cleanwizard.gradle.implementation.builders.layerConfigs

import corp.tbm.cleanwizard.gradle.api.builders.layerConfigs.CleanWizardDataLayerConfigBuilder
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardInterfaceMapperConfig
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardRoomConfig
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardRoomTypeConvertersConfig
import corp.tbm.cleanwizard.gradle.api.config.layerConfigs.CleanWizardLayerConfig

internal class CleanWizardDataLayerConfigBuilderImplementation(data: CleanWizardLayerConfig.Data) :
    CleanWizardDataLayerConfigBuilder(data) {

    private val interfaceMapperConfigBuilder = CleanWizardDataLayerInterfaceMapperConfigBuilderImplementation()
    private val roomConfigBuilder = CleanWizardDataLayerRoomConfigBuilderImplementation()

    override fun interfaceMapper(block: CleanWizardDataLayerInterfaceMapperConfigBuilder.() -> Unit) {
        interfaceMapperConfigBuilder.apply(block)
    }

    override fun room(block: CleanWizardDataLayerRoomConfigBuilder.() -> Unit) {
        roomConfigBuilder.apply(block)
    }

    internal fun build(): CleanWizardLayerConfig.Data {
        return layerConfig.copy(
            moduleName,
            classSuffix,
            packageName,
            interfaceMapperConfigBuilder.build(),
            schemaSuffix,
            nullResolutionStrategy,
            toDomainAsTopLevel,
            toDomainMapFunctionName,
            roomConfigBuilder.build()
        )
    }

    private inner class CleanWizardDataLayerInterfaceMapperConfigBuilderImplementation :
        CleanWizardDataLayerInterfaceMapperConfigBuilder() {

        fun build(): CleanWizardInterfaceMapperConfig {
            return this@CleanWizardDataLayerConfigBuilderImplementation.layerConfig.interfaceMapperConfig.copy(
                className,
                pathToModuleToGenerateInterfaceMapper,
            )
        }
    }

    private inner class CleanWizardDataLayerRoomConfigBuilderImplementation :
        CleanWizardDataLayerRoomConfigBuilder() {

        private val roomTypeConvertersConfigBuilder =
            CleanWizardDataLayerRoomTypeConvertersConfigBuilderImplementation()

        override fun typeConverters(block: CleanWizardDataLayerRoomTypeConvertersConfigBuilder.() -> Unit) {
            roomTypeConvertersConfigBuilder.apply(block)
        }

        fun build(): CleanWizardRoomConfig {
            return this@CleanWizardDataLayerConfigBuilderImplementation.layerConfig.roomConfig.copy(
                roomTypeConvertersConfigBuilder.build()
            )
        }

        private inner class CleanWizardDataLayerRoomTypeConvertersConfigBuilderImplementation :
            CleanWizardDataLayerRoomTypeConvertersConfigBuilder() {

            fun build(): CleanWizardRoomTypeConvertersConfig {
                return this@CleanWizardDataLayerConfigBuilderImplementation.layerConfig.roomConfig.roomTypeConvertersConfig.copy(
                    classSuffix,
                    packageName,
                    generateSeparateConverterForEachDTO,
                    useProvidedTypeConverter
                )
            }
        }
    }
}