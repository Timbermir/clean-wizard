package corp.tbm.cleanwizard.buildLogic.convention.implementation.layerConfigs

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfig
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardPresentationLayerConfigBuilder

internal class CleanWizardPresentationLayerConfigBuilderImplementation(presentation: CleanWizardLayerConfig.Presentation) :
    CleanWizardPresentationLayerConfigBuilder(presentation) {

    internal fun build(): CleanWizardLayerConfig.Presentation {
        return layerConfig.copy(
            moduleName,
            classSuffix,
            packageName,
            toDomainMapFunctionName
        )
    }
}