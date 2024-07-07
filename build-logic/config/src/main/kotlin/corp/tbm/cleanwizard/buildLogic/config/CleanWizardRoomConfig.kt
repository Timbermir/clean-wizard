package corp.tbm.cleanwizard.buildLogic.config

import kotlinx.serialization.Serializable

@Serializable
data class CleanWizardRoomConfig(
    val roomTypeConvertersConfig: CleanWizardRoomTypeConvertersConfig = CleanWizardRoomTypeConvertersConfig()
)