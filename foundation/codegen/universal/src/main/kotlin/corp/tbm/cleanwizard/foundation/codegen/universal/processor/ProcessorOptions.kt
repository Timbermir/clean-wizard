package corp.tbm.cleanwizard.foundation.codegen.universal.processor

object ProcessorOptions {

    private var _dataClassGenerationPattern: DataClassGenerationPattern = DataClassGenerationPattern.LAYER

    val dataClassGenerationPattern
        get() = _dataClassGenerationPattern

    private var _jsonSerializer = JsonSerializer.KOTLINX_SERIALIZATION

    val jsonSerializer: JsonSerializer
        get() = _jsonSerializer

    private var _dtoOptions = ClassGenerationConfig.DTO()

    val dtoOptions: ClassGenerationConfig.DTO
        get() = _dtoOptions

    private var _domainOptions = ClassGenerationConfig.Domain()

    val domainOptions: ClassGenerationConfig.Domain
        get() = _domainOptions

    private var _uiOptions = ClassGenerationConfig.UI()

    val uiOptions: ClassGenerationConfig.UI
        get() = _uiOptions

    fun generateConfigs(processorOptions: Map<String, String>) {
        _jsonSerializer = JsonSerializer.entries.first {
            it.serializer == processorOptions["JSON_SERIALIZER"]
        }
        _dataClassGenerationPattern =
            DataClassGenerationPattern.entries.first { it.name == processorOptions["DATA_CLASS_GENERATION_PATTERN"] }

        ClassGenerationConfig.setProcessorOptions(processorOptions)
        _dtoOptions = ClassGenerationConfig.DTO.constructConfig()
        _domainOptions = ClassGenerationConfig.Domain.constructConfig()
        _uiOptions = ClassGenerationConfig.UI.constructConfig()
    }
}