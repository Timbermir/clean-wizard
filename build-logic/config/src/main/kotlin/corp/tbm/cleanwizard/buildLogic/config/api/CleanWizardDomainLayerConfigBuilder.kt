package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfig
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardUseCaseFunctionType
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl

abstract class CleanWizardDomainLayerConfigBuilder(domain: CleanWizardLayerConfig.Domain) :
    CleanWizardLayerConfigBuilder<CleanWizardLayerConfig.Domain>(domain) {

    var toDTOMapFunctionName: String = layerConfig.toDTOMapFunctionName
    var toUIMapFunctionName: String = layerConfig.toUIMapFunctionName

    abstract fun useCase(block: CleanWizardUseCaseConfigBuilder.() -> Unit)

    @CleanWizardConfigDsl
    abstract inner class CleanWizardUseCaseConfigBuilder {

        val domain = this@CleanWizardDomainLayerConfigBuilder.layerConfig

        var classSuffix: String = domain.useCaseConfig.classSuffix
        var packageName: String = domain.useCaseConfig.packageName
        var useCaseFunctionType: CleanWizardUseCaseFunctionType = CleanWizardUseCaseFunctionType.Operator
        var createWrapper: Boolean = domain.useCaseConfig.createWrapper
    }
}