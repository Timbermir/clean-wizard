package corp.tbm.cleanwizard.buildLogic.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class CleanWizardUseCaseFunctionType {

    @Serializable
    @SerialName("Operator")
    data object Operator : CleanWizardUseCaseFunctionType()

    @Serializable
    @SerialName("InheritRepositoryFunctionName")
    data object InheritRepositoryFunctionName : CleanWizardUseCaseFunctionType()

    @Serializable
    @SerialName("CustomFunctionName")
    data class CustomFunctionName(val functionName: String) :
        CleanWizardUseCaseFunctionType()
}