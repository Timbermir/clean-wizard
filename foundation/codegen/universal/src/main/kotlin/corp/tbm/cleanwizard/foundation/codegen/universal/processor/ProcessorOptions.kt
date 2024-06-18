package corp.tbm.cleanwizard.foundation.codegen.universal.processor

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardJsonSerializer
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfig
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

    private var _dtoOptions = CleanWizardLayerConfig.Data()

    val dataConfig: CleanWizardLayerConfig.Data
        get() = _dtoOptions

    private var _domainOptions = CleanWizardLayerConfig.Domain()

    val domainConfig: CleanWizardLayerConfig.Domain
        get() = _domainOptions

    private var _uiOptions = CleanWizardLayerConfig.Presentation()

    val presentationConfig: CleanWizardLayerConfig.Presentation
        get() = _uiOptions

    fun generateConfigs(processorOptions: Map<String, String>) = with(processorOptions) {
        _dataClassGenerationPattern =
            DataClassGenerationPattern.entries.first { it.name == processorOptions["DATA_CLASS_GENERATION_PATTERN"] }
        _jsonSerializer = getFile("JSON_SERIALIZER").readJsonFromFile()
        _dependencyInjectionFramework = getFile("DEPENDENCY_INJECTION_FRAMEWORK").readJsonFromFile()
        _dtoOptions = getFile("DATA_CONFIG").readJsonFromFile()
        _domainOptions = getFile("DOMAIN_CONFIG").readJsonFromFile()
        _uiOptions = getFile("PRESENTATION_CONFIG").readJsonFromFile()
    }
}