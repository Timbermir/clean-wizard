package corp.tbm.cleanwizard.buildLogic.convention.implementation.layerConfigs

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfig
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardUseCaseConfig
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardDomainLayerConfigBuilder

internal class CleanWizardDomainLayerConfigBuilderImplementation(domain: CleanWizardLayerConfig.Domain) :
    CleanWizardDomainLayerConfigBuilder(
        domain
    ) {

    private val cleanWizardUseCaseConfigBuilder = CleanWizardUseCaseConfigBuilderImplementation()

    override fun useCase(block: CleanWizardUseCaseConfigBuilder.() -> Unit) {
        cleanWizardUseCaseConfigBuilder.apply(block)
    }

    internal fun build(): CleanWizardLayerConfig.Domain {
        return layerConfig.copy(
            moduleName,
            classSuffix,
            packageName,
            toDTOMapFunctionName,
            toUIMapFunctionName,
            cleanWizardUseCaseConfigBuilder.build()
        )
    }

    private inner class CleanWizardUseCaseConfigBuilderImplementation : CleanWizardUseCaseConfigBuilder() {

        fun build(): CleanWizardUseCaseConfig {
            return this@CleanWizardDomainLayerConfigBuilderImplementation.layerConfig.useCaseConfig.copy(
                classSuffix,
                packageName,
                useCaseFunctionType,
                createWrapper
            )
        }
    }
}