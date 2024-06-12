package corp.tbm.cleanwizard.foundation.codegen.universal

import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.domainOptions
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.dtoOptions
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.uiOptions

enum class ModelType(val moduleName: String, val suffix: String, val packageName: String) {
    DTO(dtoOptions.moduleName, dtoOptions.suffix, dtoOptions.packageName),
    MODEL(domainOptions.moduleName, domainOptions.suffix, domainOptions.packageName),
    UI(uiOptions.moduleName, uiOptions.suffix, uiOptions.packageName);
}