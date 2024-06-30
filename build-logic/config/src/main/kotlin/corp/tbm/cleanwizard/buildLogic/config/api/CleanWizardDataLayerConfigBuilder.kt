package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfig
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardRoomTypeConvertersConfig
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl

abstract class CleanWizardDataLayerConfigBuilder(data: CleanWizardLayerConfig.Data) :
    CleanWizardLayerConfigBuilder<CleanWizardLayerConfig.Data>(data) {

    abstract var schemaSuffix: String
    abstract var interfaceMapperName: String
    abstract var toDomainMapFunctionName: String

    abstract fun room(block: CleanWizardDataLayerRoomConfigBuilder.() -> Unit)
}

@CleanWizardConfigDsl
abstract class CleanWizardDataLayerRoomConfigBuilder {
    abstract fun typeConverters(block: CleanWizardRoomTypeConvertersConfig.() -> Unit)
}