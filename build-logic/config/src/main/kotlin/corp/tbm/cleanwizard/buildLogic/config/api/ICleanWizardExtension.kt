package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDataClassGenerationPattern
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfig
import corp.tbm.cleanwizard.buildLogic.config.dsl.CleanWizardConfigDsl

@CleanWizardConfigDsl
abstract class ICleanWizardExtension {

    abstract var dataClassGenerationPattern: CleanWizardDataClassGenerationPattern

    abstract fun `json-serializer`(block: ICleanWizardJsonSerializerBuilder.() -> Unit)

    abstract fun data(block: CleanWizardLayerConfig.Data.() -> Unit)

    abstract fun domain(block: CleanWizardLayerConfig.Domain.() -> Unit)

    abstract fun presentation(block: CleanWizardLayerConfig.Presentation.() -> Unit)

    abstract fun `dependency-injection`(block: ICleanWizardDependencyInjectionFrameworkBuilder.() -> Unit)
}