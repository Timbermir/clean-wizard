package corp.tbm.cleanwizard.buildLogic.config

import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl
import kotlinx.serialization.Serializable

@Serializable
@CleanWizardConfigDsl
class CleanWizardRoomTypeConvertersConfig(
    var classSuffix: String = "Converter",
    var packageName: String = "converters",
    var useProvidedTypeConverter: Boolean = false
)