package corp.tbm.cleanwizard.buildLogic.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class CleanWizardImport(
    @SerialName("packageName") open val packageName: String,
    @SerialName("name") open val name: String
) {

    @Serializable
    data class Kodein(
        @SerialName("kodeinName") override val name: String
    ) : CleanWizardImport("org.kodein.di", name)
}