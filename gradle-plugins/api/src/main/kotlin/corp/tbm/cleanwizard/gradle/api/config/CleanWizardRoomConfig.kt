package corp.tbm.cleanwizard.gradle.api.config

import kotlinx.serialization.Serializable

@Serializable
data class CleanWizardRoomConfig(
    val roomTypeConvertersConfig: CleanWizardRoomTypeConvertersConfig = CleanWizardRoomTypeConvertersConfig()
)