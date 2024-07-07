package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfig
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl

@CleanWizardConfigDsl
abstract class CleanWizardLayerConfigBuilder<T : CleanWizardLayerConfig>(private val layerConfig: T) {

    var moduleName: String
        get() = layerConfig.moduleName
        set(value) {
            layerConfig.moduleName = value
        }

    var classSuffix: String
        get() = layerConfig.classSuffix
        set(value) {
            layerConfig.classSuffix = value
        }

    var packageName: String
        get() = layerConfig.packageName
        set(value) {
            layerConfig.packageName = value
        }
}