package corp.tbm.cleanwizard.gradle.api.extensions

import corp.tbm.cleanwizard.gradle.api.annotations.CleanWizardConfigDsl
import corp.tbm.cleanwizard.gradle.api.builders.dependencyInjectionFrameworks.CleanWizardDependencyInjectionFrameworkBuilder
import corp.tbm.cleanwizard.gradle.api.builders.jsonSerializers.CleanWizardJsonSerializerBuilder
import corp.tbm.cleanwizard.gradle.api.builders.layerConfigs.CleanWizardDataLayerConfigBuilder
import corp.tbm.cleanwizard.gradle.api.builders.layerConfigs.CleanWizardDomainLayerConfigBuilder
import corp.tbm.cleanwizard.gradle.api.builders.layerConfigs.CleanWizardPresentationLayerConfigBuilder
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardDataClassGenerationPattern

@CleanWizardConfigDsl
abstract class CleanWizardExtension {

    abstract var dataClassGenerationPattern: CleanWizardDataClassGenerationPattern

    abstract fun jsonSerializer(block: CleanWizardJsonSerializerBuilder.() -> Unit)

    abstract fun data(block: CleanWizardDataLayerConfigBuilder.() -> Unit)

    abstract fun domain(block: CleanWizardDomainLayerConfigBuilder.() -> Unit)

    abstract fun presentation(block: CleanWizardPresentationLayerConfigBuilder.() -> Unit)

    abstract fun dependencyInjection(block: CleanWizardDependencyInjectionFrameworkBuilder.() -> Unit)
}