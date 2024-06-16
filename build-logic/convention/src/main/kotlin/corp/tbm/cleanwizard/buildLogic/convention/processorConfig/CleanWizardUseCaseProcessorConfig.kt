package corp.tbm.cleanwizard.buildLogic.convention.processorConfig

import corp.tbm.cleanwizard.buildLogic.convention.foundation.dsl.CleanWizardProcessorConfigDslMarker

@CleanWizardProcessorConfigDslMarker
data class CleanWizardUseCaseProcessorConfig(
    var suffix: String = "UseCase",
    var useOperatorInvoke: Boolean = true,
    var createWrapper: Boolean = false
)