package corp.tbm.cleanwizard.foundation.codegen.universal.processor

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardJsonSerializer
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfigWrapper
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.getFile
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.readJsonFromFile

object ProcessorOptions {

    private var _dataClassGenerationPattern: DataClassGenerationPattern = DataClassGenerationPattern.LAYER
    val dataClassGenerationPattern
        get() = _dataClassGenerationPattern

    private var _jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization
    val jsonSerializer: CleanWizardJsonSerializer
        get() = _jsonSerializer

    private var _dependencyInjectionFramework: CleanWizardDependencyInjectionFramework =
        CleanWizardDependencyInjectionFramework.None
    val dependencyInjectionFramework: CleanWizardDependencyInjectionFramework
        get() = _dependencyInjectionFramework

    private var _layerConfigs = CleanWizardLayerConfigWrapper()
    val layerConfigs
        get() = _layerConfigs

    fun generateConfigs(processorOptions: Map<String, String>) = with(processorOptions) {
        _dataClassGenerationPattern =
            DataClassGenerationPattern.entries.first { it.name == processorOptions["DATA_CLASS_GENERATION_PATTERN"] }
        _jsonSerializer = getFile("JSON_SERIALIZER").readJsonFromFile()
        _dependencyInjectionFramework = getFile("DEPENDENCY_INJECTION_FRAMEWORK").readJsonFromFile()
        _layerConfigs = getFile("LAYER_CONFIGS").readJsonFromFile()
    }
}