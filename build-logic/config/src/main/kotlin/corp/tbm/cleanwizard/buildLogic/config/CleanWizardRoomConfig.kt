package corp.tbm.cleanwizard.buildLogic.config

import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl
import kotlinx.serialization.Serializable

@Serializable
@CleanWizardConfigDsl
class CleanWizardRoomConfig(
    val roomTypeConvertersConfig: CleanWizardRoomTypeConvertersConfig = CleanWizardRoomTypeConvertersConfig()
)