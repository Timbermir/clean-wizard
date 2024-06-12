package corp.tbm.cleanwizard.foundation.codegen.universal

import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.domainOptions
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.dtoOptions
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.uiOptions

enum class ModelType(val suffix: String, val packageName: String) {
    DTO(dtoOptions.suffix, dtoOptions.packageName),
    MODEL(domainOptions.suffix, domainOptions.packageName),
    UI(uiOptions.suffix, uiOptions.packageName);
}