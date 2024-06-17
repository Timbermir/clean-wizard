package corp.tbm.cleanwizard.processors.useCase

import kotlinx.serialization.json.Json
import java.io.File

object UseCaseProcessorOptions {

    private var _suffix = "UseCase"
    val suffix: String
        get() = _suffix

    private var _packageName = "useCases"
    val packageName: String
        get() = _packageName

    private var _useCaseProcessorFunctionType: UseCaseProcessorFunctionType = UseCaseProcessorFunctionType.Operator
    val useCaseProcessorFunctionType: UseCaseProcessorFunctionType
        get() = _useCaseProcessorFunctionType

    private var _createWrapper = false
    val createWrapper: Boolean
        get() = _createWrapper

    fun generateConfig(processorOptions: Map<String, String>) {
        _suffix = processorOptions["USE_CASE_SUFFIX"] ?: "UseCase"
        _packageName = processorOptions["USE_CASE_PACKAGE_NAME"] ?: "useCases"
        _useCaseProcessorFunctionType =
            Json.decodeFromString<UseCaseProcessorFunctionType>(File(processorOptions["USE_CASE_FUNCTION_TYPE"]).readText())
        _createWrapper = processorOptions["USE_CASE_CREATE_WRAPPER"]?.toBooleanStrictOrNull() ?: false
    }
}