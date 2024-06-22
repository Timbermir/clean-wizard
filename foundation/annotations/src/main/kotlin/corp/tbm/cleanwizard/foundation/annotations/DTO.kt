package corp.tbm.cleanwizard.foundation.annotations

@Target(AnnotationTarget.CLASS)
annotation class DTO(
    val toDomainAsTopLevel: Boolean = true,
    val backwardsMappingConfig: BackwardsMappingConfig = BackwardsMappingConfig.NONE
)

enum class BackwardsMappingConfig {
    NONE, DOMAIN_TO_DATA, FULL_MAPPING
}