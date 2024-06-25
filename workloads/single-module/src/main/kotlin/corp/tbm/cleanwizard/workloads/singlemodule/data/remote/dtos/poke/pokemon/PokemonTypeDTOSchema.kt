package corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.poke.pokemon

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.poke.PokemonReferenceDTOSchema

@DTO
data class PokemonTypeDTOSchema(
    val slot: Int,
    val type: PokemonReferenceDTOSchema
)