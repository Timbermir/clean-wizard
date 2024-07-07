package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfig
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl

@CleanWizardConfigDsl
abstract class CleanWizardLayerConfigBuilder<C : CleanWizardLayerConfig>(protected val layerConfig: C) {

    var moduleName: String = layerConfig.moduleName

    var classSuffix: String = layerConfig.classSuffix

    var packageName: String = layerConfig.packageName
}