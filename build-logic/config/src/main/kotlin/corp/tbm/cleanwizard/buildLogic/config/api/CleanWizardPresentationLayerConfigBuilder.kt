package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfig

abstract class CleanWizardPresentationLayerConfigBuilder(presentation: CleanWizardLayerConfig.Presentation) :
    CleanWizardLayerConfigBuilder<CleanWizardLayerConfig.Presentation>(presentation) {

    abstract var toDomainMapFunctionName: String
}