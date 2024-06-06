package corp.tbm.cleanarchitecturemapper.foundation.codegen.universal

import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.ks.ProcessorOptions

enum class ModelType(val suffix: String) {
    DTO(ProcessorOptions.dtoOptions.prefix),
    MODEL(ProcessorOptions.domainOptions.prefix),
    UI(ProcessorOptions.uiOptions.prefix)
}