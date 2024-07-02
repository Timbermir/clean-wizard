package corp.tbm.cleanwizard.buildLogic.config

import kotlinx.serialization.Serializable

@Serializable
data class CleanWizardRoomTypeConvertersConfig(
    val classSuffix: String = "Converter",
    val packageName: String = "converters",
    val generateSeparateConverterForEachDTO: Boolean = true,
    val useProvidedTypeConverter: Boolean = false
)