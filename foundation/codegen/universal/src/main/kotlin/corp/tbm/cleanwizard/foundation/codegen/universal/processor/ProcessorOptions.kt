package corp.tbm.cleanwizard.foundation.codegen.universal.processor

object ProcessorOptions {

    private var _dtoOptions = ClassGenerationConfig.DTO()

    val dtoOptions: ClassGenerationConfig.DTO
        get() = _dtoOptions

    private var _domainOptions = ClassGenerationConfig.Domain()

    val domainOptions: ClassGenerationConfig.Domain
        get() = _domainOptions

    private var _uiOptions = ClassGenerationConfig.UI()

    val uiOptions: ClassGenerationConfig.UI
        get() = _uiOptions

    private var _defaultJsonSerializer = JsonSerializer.KOTLINX_SERIALIZATION

    val defaultJsonSerializer: JsonSerializer
        get() = _defaultJsonSerializer

    fun generateConfigs(processorOptions: Map<String, String>) {
        ClassGenerationConfig.setProcessorOptions(processorOptions)
        _dtoOptions = ClassGenerationConfig.DTO.constructConfig()
        _domainOptions = ClassGenerationConfig.Domain.constructConfig()
        _uiOptions = ClassGenerationConfig.UI.constructConfig()
        _defaultJsonSerializer = JsonSerializer.entries.first {
            it.serializer == processorOptions["DEFAULT_JSON_SERIALIZER"]
        }
    }
}