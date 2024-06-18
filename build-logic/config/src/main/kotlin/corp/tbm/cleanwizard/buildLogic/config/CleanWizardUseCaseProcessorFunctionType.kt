package corp.tbm.cleanwizard.buildLogic.convention.processorConfig

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
    class CustomFunctionName(@SerialName("functionName") val functionName: String) :
        CleanWizardUseCaseProcessorFunctionType()
}