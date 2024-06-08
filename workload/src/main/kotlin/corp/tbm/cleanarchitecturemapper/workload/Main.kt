package corp.tbm.cleanarchitecturemapper.workload

import corp.tbm.cleanarchitecturemapper.foundation.annotations.BackwardsMappingConfig
import corp.tbm.cleanarchitecturemapper.foundation.annotations.DTO

fun main() {
}

@DTO(backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class CarDto(
    val carName: String,
    val carValue: CarValueDto
)

@DTO(backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class CarValueDto(
    val carValue: Int
)


