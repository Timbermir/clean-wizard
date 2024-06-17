package corp.tbm.cleanwizard.processors.useCase

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class UseCaseProcessorFunctionType {

    @Serializable
    @SerialName("Operator")
    data object Operator : UseCaseProcessorFunctionType()

    @Serializable
    @SerialName("InheritRepositoryFunctionName")
    data object InheritRepositoryFunctionName : UseCaseProcessorFunctionType()

    @Serializable
    @SerialName("CustomFunctionName")
    class CustomFunctionName(val functionName: String) : UseCaseProcessorFunctionType()
}