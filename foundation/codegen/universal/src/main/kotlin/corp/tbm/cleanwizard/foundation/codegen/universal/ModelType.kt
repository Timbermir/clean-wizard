package corp.tbm.cleanwizard.foundation.codegen.universal

import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.domainConfig
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.dataConfig
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.presentationConfig

enum class ModelType(val moduleName: String, val suffix: String, val packageName: String) {
    DTO(dataConfig.moduleName, dataConfig.classSuffix, dataConfig.packageName),
    MODEL(domainConfig.moduleName, domainConfig.classSuffix, domainConfig.packageName),
    UI(presentationConfig.moduleName, presentationConfig.classSuffix, presentationConfig.packageName);
}