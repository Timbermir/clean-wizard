package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfig
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardRoomConfig
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardRoomTypeConvertersConfig
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl

abstract class CleanWizardDataLayerConfigBuilder(data: CleanWizardLayerConfig.Data) :
    CleanWizardLayerConfigBuilder<CleanWizardLayerConfig.Data>(data) {

    var schemaSuffix: String = layerConfig.schemaSuffix
    var interfaceMapperName: String = layerConfig.interfaceMapperName
    var toDomainMapFunctionName: String = layerConfig.toDomainMapFunctionName

    abstract fun room(block: CleanWizardDataLayerRoomConfigBuilder.() -> Unit)

    @CleanWizardConfigDsl
    abstract inner class CleanWizardDataLayerRoomConfigBuilder {

        val data = this@CleanWizardDataLayerConfigBuilder.layerConfig

        abstract fun typeConverters(block: CleanWizardDataLayerRoomTypeConvertersConfigBuilder.() -> Unit)

        @CleanWizardConfigDsl
        abstract inner class CleanWizardDataLayerRoomTypeConvertersConfigBuilder {
            private val roomTypeConvertersConfig =
                this@CleanWizardDataLayerConfigBuilder.layerConfig.roomConfig.roomTypeConvertersConfig

            var classSuffix: String = roomTypeConvertersConfig.classSuffix
            var packageName: String = roomTypeConvertersConfig.packageName
            var generateSeparateConverterForEachDTO: Boolean =
                roomTypeConvertersConfig.generateSeparateConverterForEachDTO
            var useProvidedTypeConverter: Boolean = roomTypeConvertersConfig.useProvidedTypeConverter
        }
    }
}