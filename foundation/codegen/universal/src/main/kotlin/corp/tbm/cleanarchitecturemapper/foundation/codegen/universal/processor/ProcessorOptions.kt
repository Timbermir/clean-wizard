package corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.processor

object ProcessorOptions {

    val dtoOptions =
        ClassGenerationConfig.DTO(
            "DTO", "dto",
            "toDomain",
            "toDTO"
        )

    val domainOptions = ClassGenerationConfig.Domain(
        "Model",
        "model"
    )

    val uiOptions = ClassGenerationConfig.UI(
        "UI",
        "ui",
        "toDomain"
    )

    val defaultJsonSerializer: JsonSerializer = JsonSerializer.KOTLINX_SERIALIZATION

    fun generateConfigs(processorOptions: Map<String, String>) {

    }
}