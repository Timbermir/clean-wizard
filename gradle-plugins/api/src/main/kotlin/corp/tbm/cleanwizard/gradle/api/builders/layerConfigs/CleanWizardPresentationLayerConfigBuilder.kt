package corp.tbm.cleanwizard.gradle.api.builders.layerConfigs

import corp.tbm.cleanwizard.gradle.api.config.layerConfigs.CleanWizardLayerConfig

abstract class CleanWizardPresentationLayerConfigBuilder(presentation: CleanWizardLayerConfig.Presentation) :
    CleanWizardLayerConfigBuilder<CleanWizardLayerConfig.Presentation>(presentation) {

    var shouldGenerate: Boolean = presentation.shouldGenerate
    var toDomainMapFunctionName: String = presentation.toDomainMapFunctionName
}