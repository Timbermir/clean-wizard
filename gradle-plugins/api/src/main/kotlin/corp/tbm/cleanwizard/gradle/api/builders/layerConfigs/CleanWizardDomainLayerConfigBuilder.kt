package corp.tbm.cleanwizard.gradle.api.builders.layerConfigs

import corp.tbm.cleanwizard.gradle.api.annotations.CleanWizardConfigDsl
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardUseCaseFunctionType
import corp.tbm.cleanwizard.gradle.api.config.layerConfigs.CleanWizardLayerConfig

abstract class CleanWizardDomainLayerConfigBuilder(domain: CleanWizardLayerConfig.Domain) :
    CleanWizardLayerConfigBuilder<CleanWizardLayerConfig.Domain>(domain) {

    var toDTOMapFunctionName: String = layerConfig.toDTOMapFunctionName
    var toUIMapFunctionName: String = layerConfig.toUIMapFunctionName

    abstract fun useCase(block: CleanWizardUseCaseConfigBuilder.() -> Unit)

    @CleanWizardConfigDsl
    abstract inner class CleanWizardUseCaseConfigBuilder {

        private val domain = this@CleanWizardDomainLayerConfigBuilder.layerConfig

        var classSuffix: String = domain.useCaseConfig.classSuffix
        var packageName: String = domain.useCaseConfig.packageName
        var useCaseFunctionType: CleanWizardUseCaseFunctionType = CleanWizardUseCaseFunctionType.Operator
        var createWrapper: Boolean = domain.useCaseConfig.createWrapper
    }
}