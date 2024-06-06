package corp.tbm.cleanarchitecturemapper.workload

import corp.tbm.cleanarchitecturemapper.foundation.annotations.BooleanEnum
import corp.tbm.cleanarchitecturemapper.foundation.annotations.DTO

fun main() {
}

@DTO
data class SosokDTOSchema(
    val name123: String,
    val debik: List<DebikDTOSchema>
)

@DTO(false)
data class DebikDTOSchema(
    val debik: String
)