package corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.pokemon

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.singlemodule.data.foundation.PokemonReferenceDTOSchema
import kotlinx.serialization.SerialName

@DTO
data class PokemonAbilityDTOSchema(
    val pokemonAbilityReference: PokemonReferenceDTOSchema,
    @SerialName("is_hidden")
    val isHidden: Boolean,
    val slot: Int,
)