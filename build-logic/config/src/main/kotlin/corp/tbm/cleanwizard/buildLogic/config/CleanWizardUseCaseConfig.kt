package corp.tbm.cleanwizard.buildLogic.config

import corp.tbm.cleanwizard.buildLogic.config.dsl.CleanWizardConfigDsl
import kotlinx.serialization.Serializable

@Serializable
@CleanWizardConfigDsl
data class CleanWizardUseCaseProcessorConfig(
    var classSuffix: String = "UseCase",
    var packageName: String = "useCases",
    var useCaseProcessorFunctionType: CleanWizardUseCaseProcessorFunctionType = CleanWizardUseCaseProcessorFunctionType.Operator,
    var createWrapper: Boolean = false
)