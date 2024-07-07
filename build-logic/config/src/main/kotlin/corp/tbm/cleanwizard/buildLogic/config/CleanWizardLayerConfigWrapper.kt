package corp.tbm.cleanwizard.buildLogic.config

import kotlinx.serialization.Serializable

@Serializable
data class CleanWizardLayerConfigWrapper(
    val data: CleanWizardLayerConfig.Data,
    val domain: CleanWizardLayerConfig.Domain,
    val presentation: CleanWizardLayerConfig.Presentation
) {

    private val configs = listOf(data, domain, presentation)

    fun first(predicate: (CleanWizardLayerConfig) -> Boolean): CleanWizardLayerConfig {
        return configs.first { predicate(it) }
    }
}