package corp.tbm.cleanwizard.gradle.api.config

import corp.tbm.cleanwizard.gradle.api.annotations.CleanWizardConfigDsl
import kotlinx.serialization.Serializable

@Serializable
@CleanWizardConfigDsl
data class CleanWizardInterfaceMapperConfig(
    val className: String = "DTOMapper",
    val pathToModuleToGenerateInterfaceMapper: String = "",
)