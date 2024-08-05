package corp.tbm.cleanwizard.gradle.implementation.builders.layerConfigs

import corp.tbm.cleanwizard.gradle.api.builders.layerConfigs.CleanWizardPresentationLayerConfigBuilder
import corp.tbm.cleanwizard.gradle.api.config.layerConfigs.CleanWizardLayerConfig

internal class CleanWizardPresentationLayerConfigBuilderImplementation(presentation: CleanWizardLayerConfig.Presentation) :
    CleanWizardPresentationLayerConfigBuilder(presentation) {

    internal fun build(): CleanWizardLayerConfig.Presentation {
        return layerConfig.copy(
            moduleName,
            classSuffix,
            packageName,
            shouldGenerate,
            toDomainMapFunctionName
        )
    }
}