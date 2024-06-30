package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfig
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardUseCaseConfig

abstract class CleanWizardDomainLayerConfigBuilder(domain: CleanWizardLayerConfig.Domain) :
    CleanWizardLayerConfigBuilder<CleanWizardLayerConfig.Domain>(domain) {

    abstract var toDTOMapFunctionName: String
    abstract var toUIMapFunctionName: String

    abstract fun useCase(block: CleanWizardUseCaseConfig.() -> Unit)
}