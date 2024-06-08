package corp.tbm.cleanarchitecturemapper.workload

import corp.tbm.cleanarchitecturemapper.foundation.annotations.BackwardsMappingConfig
import corp.tbm.cleanarchitecturemapper.foundation.annotations.DTO

fun main() {

}

@DTO(true, backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class AutoDTOSchema(
    val name: String,
    val dateRelease: DateReleaseDTOSchema,
    val list: List<DateReleaseDTOSchema>
)

@DTO(true, backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class DateReleaseDTOSchema(
    val dateRelease: Double
)


