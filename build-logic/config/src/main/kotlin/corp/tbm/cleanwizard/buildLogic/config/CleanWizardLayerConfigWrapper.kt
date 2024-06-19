package corp.tbm.cleanwizard.buildLogic.config

import kotlinx.serialization.Serializable

@Serializable
data class CleanWizardLayerConfigWrapper(
    var data: CleanWizardLayerConfig.Data = CleanWizardLayerConfig.Data(),
    var domain: CleanWizardLayerConfig.Domain = CleanWizardLayerConfig.Domain(),
    var presentation: CleanWizardLayerConfig.Presentation = CleanWizardLayerConfig.Presentation()
) {

    val configs = listOf(data, domain, presentation)

    fun first(predicate: (CleanWizardLayerConfig) -> Boolean): CleanWizardLayerConfig {
        return configs.first { predicate(it) }
    }
}