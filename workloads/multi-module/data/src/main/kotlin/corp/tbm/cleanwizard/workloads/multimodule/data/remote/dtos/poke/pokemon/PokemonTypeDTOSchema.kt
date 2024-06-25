package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.pokemon

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.multimodule.data.foundation.PokemonReferenceDTOSchema

@DTO
data class PokemonTypeDTOSchema(
    val slot: Int,
    val type: PokemonReferenceDTOSchema
)