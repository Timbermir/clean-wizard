package corp.tbm.cleanwizard.buildLogic.convention.implementation.layerConfigs

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfig
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardRoomConfig
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardRoomTypeConvertersConfig
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardDataLayerConfigBuilder

internal class CleanWizardDataLayerConfigBuilderImplementation(data: CleanWizardLayerConfig.Data) :
    CleanWizardDataLayerConfigBuilder(data) {

    private val roomConfigBuilder = CleanWizardDataLayerRoomConfigBuilderImplementation()

    override fun room(block: CleanWizardDataLayerRoomConfigBuilder.() -> Unit) {
        roomConfigBuilder.apply(block)
    }

    internal fun build(): CleanWizardLayerConfig.Data {
        return layerConfig.copy(
            moduleName,
            classSuffix,
            packageName,
            schemaSuffix,
            interfaceMapperName,
            toDomainMapFunctionName,
            roomConfigBuilder.build()
        )
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