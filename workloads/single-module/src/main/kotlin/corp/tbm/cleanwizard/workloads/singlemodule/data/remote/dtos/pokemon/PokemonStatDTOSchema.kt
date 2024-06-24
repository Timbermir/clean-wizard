package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.pokemon

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.foundation.PokemonReferenceDTOSchema
import kotlinx.serialization.SerialName

@DTO
data class PokemonStatDTOSchema(
    @SerialName("base_stat")
    val baseStat: Int,
    val effort: Int,
    val stat: PokemonReferenceDTOSchema
)