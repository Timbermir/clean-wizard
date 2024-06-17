package corp.tbm.cleanwizard.buildLogic.convention.processorConfig

import corp.tbm.cleanwizard.buildLogic.convention.foundation.dsl.CleanWizardProcessorConfigDslMarker
import kotlinx.serialization.Serializable

@Serializable
@CleanWizardProcessorConfigDslMarker
data class CleanWizardUseCaseProcessorConfig(
    var suffix: String = "UseCase",
    var packageName: String = "useCases",
    var useCaseProcessorFunctionType: CleanWizardUseCaseProcessorFunctionType = CleanWizardUseCaseProcessorFunctionType.Operator,
    var createWrapper: Boolean = false
)