package corp.tbm.cleanwizard.gradle.api.builders.layerConfigs

import corp.tbm.cleanwizard.gradle.api.annotations.CleanWizardConfigDsl
import corp.tbm.cleanwizard.gradle.api.config.layerConfigs.CleanWizardLayerConfig

@CleanWizardConfigDsl
abstract class CleanWizardLayerConfigBuilder<C : CleanWizardLayerConfig>(protected val layerConfig: C) {

    var moduleName: String = layerConfig.moduleName

    var classSuffix: String = layerConfig.classSuffix

    var packageName: String = layerConfig.packageName
}