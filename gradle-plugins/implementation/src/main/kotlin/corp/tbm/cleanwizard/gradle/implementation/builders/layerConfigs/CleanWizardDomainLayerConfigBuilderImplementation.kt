package corp.tbm.cleanwizard.gradle.implementation.builders.layerConfigs

import corp.tbm.cleanwizard.gradle.api.builders.layerConfigs.CleanWizardDomainLayerConfigBuilder
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardUseCaseConfig
import corp.tbm.cleanwizard.gradle.api.config.layerConfigs.CleanWizardLayerConfig

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