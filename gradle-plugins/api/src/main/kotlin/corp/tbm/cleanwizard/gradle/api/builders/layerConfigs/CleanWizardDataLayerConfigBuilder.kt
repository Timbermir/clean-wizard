package corp.tbm.cleanwizard.gradle.api.builders.layerConfigs

import corp.tbm.cleanwizard.gradle.api.annotations.CleanWizardConfigDsl
import corp.tbm.cleanwizard.gradle.api.config.layerConfigs.CleanWizardLayerConfig

abstract class CleanWizardDataLayerConfigBuilder(data: CleanWizardLayerConfig.Data) :
    CleanWizardLayerConfigBuilder<CleanWizardLayerConfig.Data>(data) {

    var schemaSuffix: String = layerConfig.schemaSuffix
    var toDomainMapFunctionName: String = layerConfig.toDomainMapFunctionName

    abstract fun interfaceMapper(block: CleanWizardDataLayerInterfaceMapperConfigBuilder.() -> Unit)

    abstract fun room(block: CleanWizardDataLayerRoomConfigBuilder.() -> Unit)

    @CleanWizardConfigDsl
    abstract inner class CleanWizardDataLayerInterfaceMapperConfigBuilder {

        val data = this@CleanWizardDataLayerConfigBuilder.layerConfig

        var pathToModuleToGenerateInterfaceMapper: String =
            data.interfaceMapperConfig.pathToModuleToGenerateInterfaceMapper
        var className: String = data.interfaceMapperConfig.className
    }

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