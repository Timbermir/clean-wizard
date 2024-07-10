package corp.tbm.cleanwizard.buildLogic.config

import kotlinx.serialization.Serializable

@Serializable
data class CleanWizardUseCaseConfig(
    val classSuffix: String = "UseCase",
    val packageName: String = "useCases",
    val useCaseFunctionType: CleanWizardUseCaseFunctionType = CleanWizardUseCaseFunctionType.Operator,
    val createWrapper: Boolean = false
)