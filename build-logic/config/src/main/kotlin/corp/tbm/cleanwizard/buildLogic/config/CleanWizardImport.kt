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
        @SerialName("kodeinPackageName") override val packageName: String = "org.kodein.di",
        @SerialName("kodeinName") override val name: String
    ) : CleanWizardImport(packageName, name)
}