package corp.tbm.cleanarchitecturemapper.workload

import corp.tbm.cleanarchitecturemapper.processor.annotations.DTO
import kotlinx.serialization.SerialName

fun main() {
    val fooModel = FooModel("debik", "daun")
    println(fooModel)
    val fooDTO = FooDTO("debik", "daun")
    fooDTO.toDomain().toUI()
    println(fooDTO)
}


@DTO
data class FooDTOSchema(
    @SerialName("name")
    val name: String,
    @SerialName("surname")
    val surname: String
)

@DTO
data class FooDebikDTOSchema(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("surname")
    val surname: String,
    @SerialName("lox")
    val lox: Boolean
)