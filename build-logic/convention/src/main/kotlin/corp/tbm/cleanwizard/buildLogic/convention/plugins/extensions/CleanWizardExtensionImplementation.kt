package corp.tbm.cleanwizard.buildLogic.convention.plugins.extensions

import corp.tbm.cleanwizard.buildLogic.config.*
import corp.tbm.cleanwizard.buildLogic.config.api.*
import corp.tbm.cleanwizard.buildLogic.convention.implementation.CleanWizardDependencyInjectionFrameworkBuilderImplementation
import corp.tbm.cleanwizard.buildLogic.convention.implementation.CleanWizardJsonSerializerBuilderImplementation
import corp.tbm.cleanwizard.buildLogic.convention.implementation.layerConfigs.CleanWizardDataLayerConfigBuilderImplementation
import corp.tbm.cleanwizard.buildLogic.convention.implementation.layerConfigs.CleanWizardDomainLayerConfigBuilderImplementation
import corp.tbm.cleanwizard.buildLogic.convention.implementation.layerConfigs.CleanWizardPresentationLayerConfigBuilderImplementation

internal open class CleanWizardExtensionImplementation(
    override var dataClassGenerationPattern: CleanWizardDataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER,
    internal var jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization(),
    internal var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework = CleanWizardDependencyInjectionFramework.None,
) : CleanWizardExtension() {

    private var data: CleanWizardLayerConfig.Data = CleanWizardLayerConfig.Data()
    private var domain: CleanWizardLayerConfig.Domain = CleanWizardLayerConfig.Domain()
    private var presentation: CleanWizardLayerConfig.Presentation = CleanWizardLayerConfig.Presentation()

    internal val layerConfigs by lazy {
        CleanWizardLayerConfigWrapper(data, domain, presentation)
    }

    override fun jsonSerializer(block: CleanWizardJsonSerializerBuilder.() -> Unit) {
        jsonSerializer = CleanWizardJsonSerializerBuilderImplementation().apply(block).build()
    }

    override fun data(block: CleanWizardDataLayerConfigBuilder.() -> Unit) {
        data = CleanWizardDataLayerConfigBuilderImplementation(data).apply(block).build()
    }

    override fun domain(block: CleanWizardDomainLayerConfigBuilder.() -> Unit) {
        domain = CleanWizardDomainLayerConfigBuilderImplementation(domain).apply(block).build()
    }

    override fun presentation(block: CleanWizardPresentationLayerConfigBuilder.() -> Unit) {
        presentation =
            CleanWizardPresentationLayerConfigBuilderImplementation(presentation).apply(block).build()
    }

    override fun dependencyInjection(block: CleanWizardDependencyInjectionFrameworkBuilder.() -> Unit) {
        dependencyInjectionFramework =
            CleanWizardDependencyInjectionFrameworkBuilderImplementation().apply(block).build()
    }
}