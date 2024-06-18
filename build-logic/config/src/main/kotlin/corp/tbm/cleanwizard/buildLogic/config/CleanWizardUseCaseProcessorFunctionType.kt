package corp.tbm.cleanwizard.buildLogic.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class CleanWizardUseCaseProcessorFunctionType {

    @Serializable
    @SerialName("Operator")
    data object Operator : CleanWizardUseCaseProcessorFunctionType()

    @Serializable
    @SerialName("InheritRepositoryFunctionName")
    data object InheritRepositoryFunctionName : CleanWizardUseCaseProcessorFunctionType()

    @Serializable
    @SerialName("CustomFunctionName")
    data class CustomFunctionName(val functionName: String) :
        CleanWizardUseCaseProcessorFunctionType()
}