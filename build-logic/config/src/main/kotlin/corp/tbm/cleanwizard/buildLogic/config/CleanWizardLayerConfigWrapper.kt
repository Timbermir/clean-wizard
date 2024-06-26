package corp.tbm.cleanwizard.buildLogic.config

import kotlinx.serialization.Serializable

@Serializable
data class CleanWizardLayerConfigWrapper(
    val data: CleanWizardLayerConfig.Data = CleanWizardLayerConfig.Data(),
    val domain: CleanWizardLayerConfig.Domain = CleanWizardLayerConfig.Domain(),
    val presentation: CleanWizardLayerConfig.Presentation = CleanWizardLayerConfig.Presentation()
) {

    val configs = listOf(data, domain, presentation)

    fun first(predicate: (CleanWizardLayerConfig) -> Boolean): CleanWizardLayerConfig {
        return configs.first { predicate(it) }
    }
}