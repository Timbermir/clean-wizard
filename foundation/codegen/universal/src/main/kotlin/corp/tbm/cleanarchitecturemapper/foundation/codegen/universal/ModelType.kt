package corp.tbm.cleanarchitecturemapper.foundation.codegen.universal

import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.processor.ProcessorOptions

enum class ModelType(val suffix: String) {
    DTO(ProcessorOptions.dtoOptions.suffix),
    MODEL(ProcessorOptions.domainOptions.suffix),
    UI(ProcessorOptions.uiOptions.suffix)
}