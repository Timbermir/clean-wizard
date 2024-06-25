package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.pokemon

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.multimodule.data.foundation.PokemonReferenceDTOSchema

@DTO
data class PokemonItemVersionDetailsDTOSchema(
    val rarity: Int,
    val version: PokemonReferenceDTOSchema
)