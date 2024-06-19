package corp.tbm.cleanwizard.foundation.codegen.universal

import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.layerConfigs

enum class ModelType(val moduleName: String, val suffix: String, val packageName: String) {
    DTO(
        layerConfigs.data.moduleName,
        layerConfigs.data.classSuffix,
        layerConfigs.data.packageName
    ),
    MODEL(
        layerConfigs.domain.moduleName,
        layerConfigs.domain.classSuffix,
        layerConfigs.domain.packageName
    ),
    UI(
        layerConfigs.presentation.moduleName,
        layerConfigs.presentation.classSuffix,
        layerConfigs.presentation.packageName
    );
}