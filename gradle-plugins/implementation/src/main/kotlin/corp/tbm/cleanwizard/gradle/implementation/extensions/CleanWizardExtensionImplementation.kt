package corp.tbm.cleanwizard.gradle.implementation.extensions

import corp.tbm.cleanwizard.gradle.api.builders.dependencyInjectionFrameworks.CleanWizardDependencyInjectionFrameworkBuilder
import corp.tbm.cleanwizard.gradle.api.builders.jsonSerializers.CleanWizardJsonSerializerBuilder
import corp.tbm.cleanwizard.gradle.api.builders.layerConfigs.CleanWizardDataLayerConfigBuilder
import corp.tbm.cleanwizard.gradle.api.builders.layerConfigs.CleanWizardDomainLayerConfigBuilder
import corp.tbm.cleanwizard.gradle.api.builders.layerConfigs.CleanWizardPresentationLayerConfigBuilder
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardDataClassGenerationPattern
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.gradle.api.config.layerConfigs.CleanWizardLayerConfig
import corp.tbm.cleanwizard.gradle.api.config.layerConfigs.CleanWizardLayerConfigWrapper
import corp.tbm.cleanwizard.gradle.api.config.serializer.CleanWizardJsonSerializer
import corp.tbm.cleanwizard.gradle.api.extensions.CleanWizardExtension
import corp.tbm.cleanwizard.gradle.implementation.builders.dependencyInjectionFrameworks.CleanWizardDependencyInjectionFrameworkBuilderImplementation
import corp.tbm.cleanwizard.gradle.implementation.builders.layerConfigs.CleanWizardDataLayerConfigBuilderImplementation
import corp.tbm.cleanwizard.gradle.implementation.builders.layerConfigs.CleanWizardDomainLayerConfigBuilderImplementation
import corp.tbm.cleanwizard.gradle.implementation.builders.layerConfigs.CleanWizardPresentationLayerConfigBuilderImplementation
import corp.tbm.cleanwizard.gradle.implementation.builders.serializers.CleanWizardJsonSerializerBuilderImplementation

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