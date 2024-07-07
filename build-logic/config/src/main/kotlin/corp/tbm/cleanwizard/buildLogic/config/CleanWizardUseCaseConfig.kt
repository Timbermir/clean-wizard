package corp.tbm.cleanwizard.buildLogic.config

import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl
import kotlinx.serialization.Serializable

@Serializable
@CleanWizardConfigDsl
data class CleanWizardUseCaseConfig(
    var classSuffix: String = "UseCase",
    var packageName: String = "useCases",
    var useCaseFunctionType: CleanWizardUseCaseFunctionType = CleanWizardUseCaseFunctionType.Operator,
    var createWrapper: Boolean = false
)