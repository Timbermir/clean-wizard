package corp.tbm.cleanwizard.buildLogic.convention.processorConfig

import corp.tbm.cleanwizard.buildLogic.convention.foundation.dsl.CleanWizardProcessorConfigDslMarker

@CleanWizardProcessorConfigDslMarker
open class CleanWizardProcessorConfig(
    var dataClassGenerationPattern: CleanWizardDataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER,
    var jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KOTLINX_SERIALIZATION,
    internal var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework = CleanWizardDependencyInjectionFramework.NONE,
    internal var dtoConfig: CleanWizardDataClassGenerationProcessorConfig.DTO = CleanWizardDataClassGenerationProcessorConfig.DTO(),
    internal var domainConfig: CleanWizardDataClassGenerationProcessorConfig.Domain = CleanWizardDataClassGenerationProcessorConfig.Domain(),
    internal var presentationConfig: CleanWizardDataClassGenerationProcessorConfig.Presentation = CleanWizardDataClassGenerationProcessorConfig.Presentation(),
    internal var useCaseConfig: CleanWizardUseCaseProcessorConfig = CleanWizardUseCaseProcessorConfig()
) {

    fun dto(configuration: CleanWizardDataClassGenerationProcessorConfig.DTO.() -> Unit) {
        dtoConfig.apply(configuration)
    }

    fun domain(configuration: CleanWizardDataClassGenerationProcessorConfig.Domain.() -> Unit) {
        domainConfig.apply(configuration)
    }

    fun presentation(configuration: CleanWizardDataClassGenerationProcessorConfig.Presentation.() -> Unit) {
        presentationConfig.apply(configuration)
    }

    fun useCase(configuration: CleanWizardUseCaseProcessorConfig.() -> Unit) {
        useCaseConfig.apply(configuration)
    }
}