package corp.tbm.cleanwizard.buildLogic.config

import corp.tbm.cleanwizard.buildLogic.config.dsl.CleanWizardConfigDslMarker
import kotlinx.serialization.Serializable

@Serializable
@CleanWizardConfigDslMarker
data class CleanWizardUseCaseProcessorConfig(
    var classSuffix: String = "UseCase",
    var packageName: String = "useCases",
    var useCaseProcessorFunctionType: CleanWizardUseCaseProcessorFunctionType = CleanWizardUseCaseProcessorFunctionType.Operator,
    var createWrapper: Boolean = false
)