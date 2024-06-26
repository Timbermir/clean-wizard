package corp.tbm.cleanwizard.foundation.codegen.universal.processor

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardJsonSerializer
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfigWrapper
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.getFile
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.readJsonFromFile

object ProcessorOptions {
    private var processorOptions = mapOf<String, String>()

    val dataClassGenerationPattern: DataClassGenerationPattern by lazy {
        DataClassGenerationPattern.entries.first { it.name == processorOptions["DATA_CLASS_GENERATION_PATTERN"] }
    }

    val jsonSerializer: CleanWizardJsonSerializer by lazy {
        processorOptions.getFile("JSON_SERIALIZER").readJsonFromFile()
    }

    val dependencyInjectionFramework: CleanWizardDependencyInjectionFramework by lazy {
        processorOptions.getFile("DEPENDENCY_INJECTION_FRAMEWORK").readJsonFromFile()
    }

    val layerConfigs: CleanWizardLayerConfigWrapper by lazy {
        processorOptions.getFile("LAYER_CONFIGS").readJsonFromFile()
    }

    fun generateConfigs(processorOptions: Map<String, String>) {
        this.processorOptions = processorOptions
    }
}