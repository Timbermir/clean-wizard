package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDataClassGenerationPattern
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl

@CleanWizardConfigDsl
abstract class CleanWizardExtension {

    abstract var dataClassGenerationPattern: CleanWizardDataClassGenerationPattern

    abstract fun `json-serializer`(block: CleanWizardJsonSerializerBuilder.() -> Unit)

    abstract fun data(block: CleanWizardDataLayerConfigBuilder.() -> Unit)

    abstract fun domain(block: CleanWizardDomainLayerConfigBuilder.() -> Unit)

    abstract fun presentation(block: CleanWizardPresentationLayerConfigBuilder.() -> Unit)

    abstract fun `dependency-injection`(block: CleanWizardDependencyInjectionFrameworkBuilder.() -> Unit)
}